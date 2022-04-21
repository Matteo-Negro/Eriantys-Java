package it.polimi.ingsw.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.utilities.exceptions.FullGameException;
import it.polimi.ingsw.utilities.exceptions.GameNotFoundException;

import java.io.IOException;
import java.net.Socket;

/**
 * Matchmaking class: used to create a multithread interface between a user and the main server object.
 *
 * @author Riccardo Milici
 */
public class Matchmaking extends Thread{

    private User user;
    private final Server gameServer;


    /**
     *
     * @param userSocket
     * @param gameServer
     */
    public Matchmaking(Socket userSocket, Server gameServer){

        this.user = null;
        this.gameServer = gameServer;

        try {
            this.user = new User(userSocket);
        }catch(IOException ioe){
            user.setConnected(false);
        }
    }


    /**
     *
     */
    public void run() {
        manageCommand();
    }


    /**
     *
     */
    private void manageCommand() {

        JsonObject command = null;

        while(user.isConnected()) {

            //Get a command from the user
            try {
                command = user.getCommand();
            }catch(IOException | ClassNotFoundException ioe) {
                user.setConnected(false);
                return;
            }

            // COMMAND PARSING
            JsonElement type = command.get("type");

            if(type.toString().equals("gameCreation")) {

                //Create the game
                String gameCode = createGame(command.get("playersNumber").getAsInt(), command.get("expert").getAsBoolean());

                //Send a game creation reply
                JsonObject gameCreationReply = new JsonObject();
                gameCreationReply.addProperty("type", "gameCreation");
                gameCreationReply.addProperty("code", gameCode);
                try{
                    user.sendCommand(gameCreationReply);
                }catch(IOException ioe){
                    user.setConnected(false);
                    return;
                }

                //Join the game
                try {
                    searchGame(gameCode);
                }catch(FullGameException fge) {

                    //Send a game NOT found reply
                    JsonObject gameNotFoundReply = new JsonObject();
                    gameNotFoundReply.addProperty("type", "enterGame");
                    gameNotFoundReply.addProperty("found", true);
                    try {
                        user.sendCommand(gameNotFoundReply);
                    }catch(IOException ioe) {
                        user.setConnected(false);
                        return;
                    }
                }

            }
            else if(type.toString().equals("enterGame")) {

                try {
                    searchGame(command.get("code").toString());
                }catch(FullGameException fge) {

                    //Send a game NOT found reply
                    JsonObject gameNotFoundReply = new JsonObject();
                    gameNotFoundReply.addProperty("type", "enterGame");
                    gameNotFoundReply.addProperty("found", true);
                    try {
                        user.sendCommand(gameNotFoundReply);
                    }catch(IOException ioe) {
                        user.setConnected(false);
                        return;
                    }
                }

            }
        }
    }


    /**
     *
     * @param playersNumber
     * @param expertMode
     * @return
     */
    private String createGame(int playersNumber, boolean expertMode) {

        return gameServer.addGame(playersNumber, expertMode);
    }


    /**
     *
     * @param gameId
     */
    private void searchGame(String gameId) throws FullGameException {

        GameController desiredGame = null;

        //Search the game
        try {
            desiredGame = gameServer.findGame(gameId);
        } catch (GameNotFoundException gnfe) {

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
            player.addProperty("name", savedPlayer.getName());
            player.addProperty("online", savedPlayer.isConnected());
            playersList.add(player);
        }

        try {
            user.sendCommand(gameFoundReply);
        } catch (IOException ioe) {
            user.setConnected(false);
        }

        login(desiredGame);
    }


    /**
     *
     */
    private void login(GameController desiredGame) {


        JsonObject playerEntranceMessage;
        boolean loggedIn = false;
        boolean isPresent = false;


        while(!loggedIn && user.isConnected()) {


            //Wait for a message from the user
            do {
                try {
                    playerEntranceMessage = user.getCommand();
                } catch (IOException | ClassNotFoundException ioe) {
                    user.setConnected(false);
                    return;
                }
            } while (!playerEntranceMessage.get("type").toString().equals("login") && !playerEntranceMessage.get("type").toString().equals("exit"));


            // A LOGIN MESSAGE ARRIVED
            if (playerEntranceMessage.get("type").toString().equals("login")) {

                user.putName(playerEntranceMessage.get("name").getAsString());

                for (User savedPlayer : desiredGame.getUsers()) {

                    if (user.getName().equals(savedPlayer.getName()) && !savedPlayer.isConnected()) {
                        isPresent = true;
                        desiredGame.getUsers().remove(savedPlayer);
                        loggedIn = true;
                        break;
                    } else {

                        if (user.getName().equals(savedPlayer.getName()) && savedPlayer.isConnected()) {

                            //Send a notAccessible reply
                            isPresent = true;
                            //Send a login not success message
                            JsonObject loginNotSuccess = new JsonObject();
                            loginNotSuccess.addProperty("type", "login");
                            loginNotSuccess.addProperty("success", false);
                            try {
                                user.sendCommand(loginNotSuccess);
                            }catch(IOException ioe) {
                                user.setConnected(false);
                                return;
                            }
                            break;
                        }
                    }
                }
                if(!isPresent && desiredGame.getUsers().size()<desiredGame.getExpectedPlayers()) loggedIn = true;

            }
            else {

                //AN EXIT MESSAGE ARRIVED
                if(playerEntranceMessage.get("type").toString().equals("exit")) return;
            }
        }

        gameServer.joinGame(user, desiredGame);

        //Send a login success message
        JsonObject loginSuccess = new JsonObject();
        loginSuccess.addProperty("type", "login");
        loginSuccess.addProperty("success", true);
        try {
            user.sendCommand(loginSuccess);
        }catch(IOException ioe) {
            user.setConnected(false);
        }
    }
}

