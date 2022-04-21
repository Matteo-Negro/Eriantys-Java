package it.polimi.ingsw.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.network.server.Server;
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
            user.setIsConnected(false);
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

        while(true) {

            //Get a command from the user
            try {
                command = user.getCommand();
            }catch(IOException | ClassNotFoundException ioe) {
                user.setIsConnected(false);
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
                    user.setIsConnected(false);
                }

                //Join the game
                searchGame(gameCode);
            }
            else if(type.toString().equals("enterGame")) {

                searchGame(command.get("code").toString());
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
    private void searchGame(String gameId) {

        GameController desiredGame = null;
        JsonObject playerEntranceMessage = null;
        boolean loggedIn = false;
        boolean isPresent = false;

        try {
            desiredGame = gameServer.findGame(gameId);
        }catch(GameNotFoundException gnfe) {

            //Send a game NOT found reply
            JsonObject gameNotFoundReply = new JsonObject();
            gameNotFoundReply.addProperty("type", "enterGame");
            gameNotFoundReply.addProperty("found", true);
            try {
                user.sendCommand(gameNotFoundReply);
            }catch(IOException ioe) {
                user.setIsConnected(false);
            }
            return;
        }

        //Send a game found reply
        JsonObject gameFoundReply = new JsonObject();
        gameFoundReply.addProperty("type", "enterGame");
        gameFoundReply.addProperty("found", true);
        gameFoundReply.addProperty("expectedPlayers", desiredGame.getExpectedPlayers);
        JsonArray playersList = new JsonArray();
        for(User savedPlayer : desiredGame.getUsers()){

            JsonObject player = new JsonObject();
            player.addProperty("name", savedPlayer.getName());
            player.addProperty("online", savedPlayer.getIsConnected());
            playersList.add(player);
        }

        try {
            user.sendCommand(gameFoundReply);
        }catch(IOException ioe) {
            user.setIsConnected(false);
        }


        // LOGGING IN
        while(!loggedIn) {

            //Wait for a message from the user
            do {
                try {
                    playerEntranceMessage = user.getCommand();
                } catch (IOException | ClassNotFoundException ioe) {
                    user.setIsConnected(false);
                }
            } while (!playerEntranceMessage.get("type").toString().equals("login") && !playerEntranceMessage.get("type").toString().equals("exit"));

            // A LOGIN MESSAGE ARRIVED
            if (playerEntranceMessage.get("type").toString().equals("login")) {

                user.putName(playerEntranceMessage.get("name").getAsString());

                for (User savedPlayer : desiredGame.getUsers()) {

                    if (user.getName().equals(savedPlayer.getName()) && !savedPlayer.getIsConnected()) {

                        isPresent = true;
                        desiredGame.getUsers().remove(savedPlayer);
                        loggedIn = true;
                    } else {

                        if (user.getName().equals(savedPlayer.getName()) && savedPlayer.getIsConnected()) {

                            //Send a notAccessible reply
                            isPresent = true;
                            JsonObject notAccessibleReply = new JsonObject();
                            gameFoundReply.addProperty("type", "notAccessibleGame");
                            gameFoundReply.addProperty("message", "The player has already joined the game.");
                            try {
                                user.sendCommand(notAccessibleReply);
                            }catch(IOException ioe) {
                                user.setIsConnected(false);
                            }
                            break;
                        }
                    }
                }
                if(!isPresent && desiredGame.getUsers().size()<desiredGame.getExpectedPlayers()) {

                    loggedIn = true;
                }

            }
            else {
                //AN EXIT MESSAGE ARRIVED
                if(playerEntranceMessage.get("type").toString().equals("exit")) {
                    return;
                }
            }
        }

        gameServer.joinGame(user, desiredGame);
    }
}
