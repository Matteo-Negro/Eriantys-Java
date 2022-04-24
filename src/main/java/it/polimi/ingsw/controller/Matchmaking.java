package it.polimi.ingsw.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.utilities.exceptions.FullGameException;

import java.io.IOException;
import java.net.Socket;

/**
 * A multithreading interface between a user and the main server object.
 *
 * @author Riccardo Milici
 */
public class Matchmaking extends Thread {

    private User user;
    private final Server gameServer;
    private final JsonObject ping;


    /**
     * The class constructor.
     * Creates an instance of the class containing the user connected, a reference to the game server and a standard ping message.
     *
     * @param userSocket The user's tcp connection socket.
     * @param gameServer The main game server object's reference.
     */
    public Matchmaking(Socket userSocket, Server gameServer) {

        this.user = null;
        this.gameServer = gameServer;
        this.ping = new JsonObject();
        ping.addProperty("type", "ping");

        try {
            this.user = new User(userSocket);
        } catch (IOException ioe) {
            this.user.setConnected(false);
        }
    }


    /**
     * Runs the "handleCommunication()" method in a new thread, while the user remains connected.
     */
    public void run() {

        while (user.isConnected()) {
            handleCommunication();
        }

    }

    /**
     * Manages the ping between server and user's client; calls the "manageCommand(JsonObject command)" method if a command message is received.
     */
    private void handleCommunication() {

        //Send a ping message to the client.
        try {
            user.sendCommand(ping);
        } catch (IOException ioe) {
            user.setConnected(false);
            return;
        }

        //Get a message from the client
        JsonObject incomingMessage;
        try {
            incomingMessage = user.getCommand();
        } catch (IOException | ClassNotFoundException e) {
            //If socket time out expires.
            user.setConnected(false);
            return;
        }

        //User is connected and wants a command to be executed.
        if (!incomingMessage.get("type").toString().equals("pong")) manageCommand(incomingMessage);
    }

    /**
     * Manages the user's command parsing and calls the "createGame(int playersNumber, boolean expertMode)" method or "searchGame(String gameCode)" method if requested.
     *
     * @param command The command to manage.
     */
    private void manageCommand(JsonObject command) {

        // COMMAND PARSING
        JsonElement type = command.get("type");

        if (type.toString().equals("gameCreation")) {

            //Create the game
            String gameCode = createGame(command.get("playersNumber").getAsInt(), command.get("expert").getAsBoolean());

            //Send a game creation reply
            JsonObject gameCreationReply = new JsonObject();
            gameCreationReply.addProperty("type", "gameCreation");
            gameCreationReply.addProperty("code", gameCode);
            try {
                user.sendCommand(gameCreationReply);
            } catch (IOException ioe) {
                user.setConnected(false);
                return;
            }

            //Join the game
            try {
                searchGame(gameCode);
            } catch (FullGameException fge) {

                //Send a game NOT found reply
                JsonObject gameNotFoundReply = new JsonObject();
                gameNotFoundReply.addProperty("type", "enterGame");
                gameNotFoundReply.addProperty("found", false);
                try {
                    user.sendCommand(gameNotFoundReply);
                } catch (IOException ioe) {
                    user.setConnected(false);
                }
            }

        } else {
            if (type.toString().equals("enterGame")) {

                try {
                    searchGame(command.get("code").toString());
                } catch (FullGameException fge) {

                    //Send a game NOT found reply
                    JsonObject gameNotFoundReply = new JsonObject();
                    gameNotFoundReply.addProperty("type", "enterGame");
                    gameNotFoundReply.addProperty("found", true);
                    try {
                        user.sendCommand(gameNotFoundReply);
                    } catch (IOException ioe) {
                        user.setConnected(false);
                    }
                }
            }
        }
    }


    /**
     * Calls the gameServer's "addGame(int expectedPlayers, boolean expertMode)" method, in order to create the game requested by the user.
     *
     * @param playersNumber The game's players number.
     * @param expertMode    Set to true if the expert game difficulty is requested.
     * @return The alphanumerical code associated with the created game.
     */
    private String createGame(int playersNumber, boolean expertMode) {

        return gameServer.addGame(playersNumber, expertMode);
    }


    /**
     * Calls the gameServer's "findGame(String gameCode)" method in order to search for a specified game; calls the "login(GameController desiredGame)" method if the game has been found.
     *
     * @param gameCode The alphanumerical code associated with the game searched.
     */
    private void searchGame(String gameCode) throws FullGameException {

        GameController desiredGame;

        //Search the game
        desiredGame = gameServer.findGame(gameCode);

        if (desiredGame == null) {
            //Send a game NOT found reply
            JsonObject gameNotFoundReply = new JsonObject();
            gameNotFoundReply.addProperty("type", "enterGame");
            gameNotFoundReply.addProperty("found", true);
            try {
                user.sendCommand(gameNotFoundReply);
            } catch (IOException ioe) {
                user.setConnected(false);
                return;
            }
            return;
        }

        //Throw an exception if the searched game is already full and active.
        if (desiredGame.getIsRunning()) throw new FullGameException();

        //Send a game found reply.
        JsonObject gameFoundReply = new JsonObject();
        gameFoundReply.addProperty("type", "enterGame");
        gameFoundReply.addProperty("found", true);
        gameFoundReply.addProperty("expectedPlayers", desiredGame.getExpectedPlayers());
        JsonArray playersList = new JsonArray();
        for (User savedPlayer : desiredGame.getUsers()) {

            JsonObject player = new JsonObject();
            player.addProperty("name", savedPlayer.getUsername());
            player.addProperty("online", savedPlayer.isConnected());
            playersList.add(player);
        }
        gameFoundReply.add("players", playersList);

        try {
            user.sendCommand(gameFoundReply);
        } catch (IOException ioe) {
            user.setConnected(false);
        }

        login(desiredGame);
    }


    /**
     * Manages the process regarding the login of the user to a specific game.
     *
     * @param desiredGame The instance of the game in which the user wants to log in.
     */
    private void login(GameController desiredGame) {


        JsonObject playerEntranceMessage;
        boolean loggedIn = false;
        boolean isPresent = false;


        while (!loggedIn && user.isConnected()) {


            //Wait for a message from the user
            do {
                try {
                    playerEntranceMessage = user.getCommand();
                } catch (IOException | ClassNotFoundException ioe) {
                    //If socket timeout expires.
                    user.setConnected(false);
                    return;
                }
            } while (!playerEntranceMessage.get("type").toString().equals("login") && !playerEntranceMessage.get("type").toString().equals("exit"));


            // A LOGIN MESSAGE ARRIVED
            if (playerEntranceMessage.get("type").toString().equals("login")) {

                user.putName(playerEntranceMessage.get("name").getAsString());

                for (User savedPlayer : desiredGame.getUsers()) {

                    if (user.getUsername().equals(savedPlayer.getUsername()) && !savedPlayer.isConnected()) {
                        isPresent = true;
                        desiredGame.getUsers().remove(savedPlayer);
                        loggedIn = true;
                        break;
                    } else {

                        if (user.getUsername().equals(savedPlayer.getUsername()) && savedPlayer.isConnected()) {

                            isPresent = true;
                            //Send a login not success message
                            JsonObject loginNotSuccess = new JsonObject();
                            loginNotSuccess.addProperty("type", "login");
                            loginNotSuccess.addProperty("success", false);
                            try {
                                user.sendCommand(loginNotSuccess);
                            } catch (IOException ioe) {
                                user.setConnected(false);
                                return;
                            }
                            break;
                        }
                    }
                }
                if (!isPresent && desiredGame.getUsers().size() < desiredGame.getExpectedPlayers()) loggedIn = true;

            } else {

                //AN EXIT MESSAGE ARRIVED
                if (playerEntranceMessage.get("type").toString().equals("exit")) return;
            }
        }

        desiredGame.addUser(user);

        //Send a login success message
        JsonObject loginSuccess = new JsonObject();
        loginSuccess.addProperty("type", "login");
        loginSuccess.addProperty("success", true);
        try {
            user.sendCommand(loginSuccess);
        } catch (IOException ioe) {
            user.setConnected(false);
        }
    }
}

