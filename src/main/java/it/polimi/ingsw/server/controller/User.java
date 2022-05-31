package it.polimi.ingsw.server.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.utilities.Log;
import it.polimi.ingsw.utilities.MessageCreator;
import it.polimi.ingsw.utilities.exceptions.FullGameException;
import it.polimi.ingsw.utilities.exceptions.GameNotFoundException;
import it.polimi.ingsw.utilities.exceptions.IllegalMoveException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * The entity containing the tcp connection socket of the client connected to the game server and its input and output streams.
 *
 * @author Riccardo Milici
 * @author Riccardo Motta
 * @author Matteo Negro
 */
public class User extends Thread {

    private final Object connectedLock;
    private final BufferedReader inputStream;
    private final PrintWriter outputStream;
    private final Server server;
    private final Ping ping;
    private boolean connected;
    private GameController gameController;
    private String username;
    private boolean logged;

    /**
     * Class constructor.
     * Creates an instance of the class, containing the username, tcp socket and its input and output streams.
     *
     * @param socket The user's tcp socket, used to communicate with the game server throw the network.
     * @param server The main server instance.
     * @throws IOException Thrown if an error occurs during the input and output streams' opening.
     */
    public User(Socket socket, Server server) throws IOException {
        this.connected = true;
        this.server = server;
        this.connectedLock = new Object();
        this.ping = new Ping(this);
        this.gameController = null;
        this.username = null;
        this.logged = false;

        inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        outputStream = new PrintWriter(socket.getOutputStream());
        socket.setSoTimeout(10000);
    }

    /**
     * Returns the name associated to the user.
     *
     * @return username attribute.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Every time checks if the user is online and when receives a command, processes it.
     */
    @Override
    public void run() {
        Log.info("User running");
        JsonObject incomingMessage;

        this.ping.start();

        while (true) {
            synchronized (connectedLock) {
                if (!connected)
                    break;
            }

            try {
                incomingMessage = getCommand();
                if (!incomingMessage.get("type").getAsString().equals("pong") && !incomingMessage.get("type").getAsString().equals("error"))
                    manageCommand(incomingMessage);
                if (this.gameController != null && !this.gameController.isFull() && this.isLogged()) {
                    synchronized (connectedLock) {
                        try {
                            this.connectedLock.wait(1500);
                        } catch (InterruptedException e) {
                            this.disconnected();
                        }
                    }
                    this.sendMessage(MessageCreator.waitingRoomUpdate(this.gameController));
                    Log.debug("waiting room message sent.");
                }
            } catch (Exception e) {
                // If socket time out expires.
                disconnected();
            }
        }
    }

    /**
     * Reads the incoming message from the tcp socket.
     *
     * @return A JsonObject containing the command received.
     * @throws IOException Thrown if an error occurs during the socket input stream read.
     */
    private synchronized JsonObject getCommand() throws IOException {
        return JsonParser.parseString(inputStream.readLine()).getAsJsonObject();
    }

    /**
     * Sends a message to the user client through the tcp socket.
     *
     * @param command A JsonObject containing the command to send.
     */
    public void sendMessage(JsonObject command) {
        synchronized (outputStream) {
            outputStream.println(command.toString());
            outputStream.flush();
        }
    }

    /**
     * Manages the user's command parsing and calls the "createGame(int playersNumber, boolean expertMode)" method or "searchGame(String gameCode)" method if requested.
     *
     * @param command The command to manage.
     * @throws IllegalMoveException Thrown if the player wants to do something illegal.
     */
    private void manageCommand(JsonObject command) throws IllegalMoveException {
        Log.debug(command.toString());
        switch (command.get("type").getAsString()) {
            case "ping" -> sendMessage(MessageCreator.pong());
            case "gameCreation" -> {
                Log.info("GameCreation message arrived");
                sendMessage(MessageCreator.gameCreation(Matchmaking.gameCreation(command, server)));
            }
            case "enterGame" -> {
                Log.info("enterGame message arrived");
                try {
                    gameController = Matchmaking.enterGame(command.get("code").getAsString(), server);
                } catch (FullGameException | GameNotFoundException e) {
                    gameController = null;
                }
                sendMessage(MessageCreator.enterGame(gameController));
                Log.debug(MessageCreator.enterGame(gameController).toString());
            }
            case "login" -> {
                Log.info("login message message arrived");
                setLogged(Matchmaking.login(gameController, command.get("name").getAsString(), this));
                sendMessage(MessageCreator.login(this.isLogged()));
                if (this.isLogged()) {
                    this.username = command.get("name").getAsString();
                    Log.debug("login reply sent: logged ");
                    this.gameController.checkStartCondition();
                }
            }
            case "logout" -> {
                Log.info("logout message message arrived");
                removeFromGame();
            }
            case "command" -> {
                switch (command.get("subtype").getAsString()) {
                    case "playAssistant" -> {
                        Log.debug("PlayAssistant command arrived.");
                        this.gameController.playAssistantCard(command.get("player").getAsString(), command.get("assistant").getAsInt());
                    }

                    case "move" -> {
                        switch (command.get("pawn").getAsString()) {
                            case "student" -> this.gameController.moveStudent(command);
                            case "motherNature" ->
                                    this.gameController.moveMotherNature(command.get("island").getAsInt());

                        }
                    }
                    case "ban" -> this.gameController.setBan(command.get("island").getAsInt());
                    case "pay" -> this.gameController.paySpecialCharacter(command);
                    case "refill" -> this.gameController.chooseCloud(command);
                }
            }
            default -> disconnected();
        }
    }

    /**
     * Manages the user disconnection after a network problem.
     */
    public void disconnected() {
        synchronized (connectedLock) {
            connected = false;
        }
        this.ping.stopPing();
        removeFromGame();
    }

    /**
     * Returns the boolean value of logged attribute.
     *
     * @return True if the user has successfully logged into a game, false otherwise.
     */
    private boolean isLogged() {
        return this.logged;
    }

    /**
     * Sets the logged attribute to the given status.
     *
     * @param status The new logged value.
     */
    private void setLogged(boolean status) {
        this.logged = status;
    }

    /**
     * If the user was in a game, s/he's removed from the game and the username is reset.
     */
    private void removeFromGame() {
        Log.info("User disconnected with name: " + this.getUsername());
        if (gameController == null) return;
        Log.debug("Removing user from game.");
        gameController.removeUser(this);
        this.gameController = null;
        this.username = null;
        setLogged(false);
    }
}
