package it.polimi.ingsw.client.controller;

import com.google.gson.*;
import it.polimi.ingsw.client.model.GameModel;
import it.polimi.ingsw.utilities.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents an interface for the client to the network, in order to handle
 * the communication between client and server.
 *
 * @author Riccardo Milici
 * @author Riccardo Motta
 * @author Matteo Negro
 */
public class GameServer implements Runnable {

    private final BufferedReader inputStream;
    private final PrintWriter outputStream;
    private final ClientController client;
    private final Object connectedLock;
    private final Ping ping;
    private boolean connected;

    /**
     * Default class constructor.
     *
     * @param hostSocket The TCP socket associated with the current connection.
     * @param client     The ClientController instance that's currently running.
     * @throws IOException Thrown if an error occurs during the input and output streams creation.
     */
    public GameServer(Socket hostSocket, ClientController client) throws IOException {
        this.inputStream = new BufferedReader(new InputStreamReader(hostSocket.getInputStream()));
        this.outputStream = new PrintWriter(hostSocket.getOutputStream());
        this.client = client;
        this.connected = true;
        this.connectedLock = new Object();
        this.ping = new Ping(this);
        Log.info("GameServer instance created");
    }

    /**
     * The main GameServer method, gets the messages from the server through the network.
     */
    @Override
    public void run() {
        new Thread(ping).start();
        JsonObject incomingMessage;
        boolean loop = true;

        while (loop) {

            synchronized (connectedLock) {
                if (!connected) break;
            }

            try {
                incomingMessage = getMessage();
                if (!incomingMessage.get("type").getAsString().equals("ping")) manageMessage(incomingMessage);
            } catch (Exception e) {
                Log.error(e);
                loop = false;
            }
        }

        this.client.setClientState(ClientState.CONNECTION_LOST);
    }

    /**
     * Reads the type of the incoming message and calls the correct method to manage it.
     *
     * @param incomingMessage The incoming message from the server.
     */
    public void manageMessage(JsonObject incomingMessage) {
        switch (incomingMessage.get("type").getAsString()) {
            case "gameCreation" -> {
                Log.debug("gameCreation reply");
                sendCommand(MessageCreator.enterGame(incomingMessage.get("code").getAsString()));
                this.client.setGameCode(incomingMessage.get("code").getAsString());
            }
            case "enterGame" -> manageEnterGame(incomingMessage);
            case "waitingRoomUpdate" -> manageWaitingRoomUpdate(incomingMessage);
            case "login" -> {
                Log.debug("login reply");
                manageLogin(incomingMessage);
            }
            case "status" -> {
                Log.debug("status message arrived");
                manageStatus(incomingMessage);
            }
            case "gameStart" -> {
                Log.debug("gameStart message arrived");
                this.client.setClientState(ClientState.GAME_RUNNING);
            }
            case "turnEnable" -> {
                Log.debug("turnEnable message arrived");
                this.manageTurnEnable(incomingMessage);
            }
            case "win" -> {
                Log.debug("endGame message arrived");
                this.manageEndGame(incomingMessage);
            }
            case "error" -> {
                Log.debug("Error message arrived: " + incomingMessage.get("message").getAsString());
                this.manageError(incomingMessage);
            }
            default -> this.client.setClientState(ClientState.CONNECTION_LOST);
        }
    }

    /**
     * Manages the "enterGame" reply message.
     *
     * @param message The message.
     */
    private void manageEnterGame(JsonObject message) {
        if (this.client.getClientState().equals(ClientState.GAME_CREATION) || this.client.getClientState().equals(ClientState.JOIN_GAME)) {
            if (message.get("found").getAsBoolean()) {
                Log.debug("enterGame reply");
                parseEnterGame(message);
                this.client.setClientState(ClientState.GAME_LOGIN);
                Log.debug("changed state Game login.");
            }
            synchronized (this.client.getLock()) {
                this.client.setReplyArrived();
                this.client.getLock().notifyAll();
            }
        }
    }

    /**
     * Helper function which parses the "enterGame" reply message from the server, initializing the GameModel.
     *
     * @param message The message to parse.
     */
    private void parseEnterGame(JsonObject message) {
        Map<String, Boolean> waitingRoom = new HashMap<>();

        int expectedPlayers = message.get("expectedPlayers").getAsInt();
        JsonArray players = message.get("players").getAsJsonArray();

        for (int i = 0; i < players.size(); i++) {

            String player = players.get(i).getAsJsonObject().get("name").getAsString();
            boolean online = players.get(i).getAsJsonObject().get("online").getAsBoolean();
            waitingRoom.put(player, online);

        }
        GameModel newGameModel = new GameModel(expectedPlayers, waitingRoom);
        this.client.initializeGameModel(newGameModel);
    }

    /**
     * Manages the "waitingRoomUpdate" message.
     *
     * @param message The message.
     */
    private void manageWaitingRoomUpdate(JsonObject message) {
        if (this.client.getClientState().equals(ClientState.GAME_WAITING_ROOM)) parseEnterGame(message);
    }

    /**
     * Manages the "login" reply message.
     *
     * @param message The message.
     */
    private void manageLogin(JsonObject message) {
        if (this.client.getClientState().equals(ClientState.GAME_LOGIN)) {
            if (message.get("success").getAsBoolean()) {
                this.client.setClientState(ClientState.GAME_WAITING_ROOM);
                Log.debug("changed state Waiting room.");
            } else {
                this.client.errorOccurred("Invalid username");
            }
        } else {
            Log.debug("changed state Connection lost.");
            this.client.setClientState(ClientState.CONNECTION_LOST);
        }

        synchronized (this.client.getLock()) {
            this.client.setReplyArrived();
            this.client.getLock().notifyAll();
        }
    }

    /**
     * Manages the "status" message, creating an updated GameModel instance.
     *
     * @param message The message.
     */
    private void manageStatus(JsonObject message) {
        int round = message.get("round").getAsInt() + 1;
        String activeUser = null;
        if (!(message.get("activeUser") instanceof JsonNull)) activeUser = message.get("activeUser").getAsString();
        boolean expert = message.get("expert").getAsBoolean();
        Phase phase = Phase.valueOf(message.get("phase").getAsString());
        GameControllerState subphase = GameControllerState.valueOf(message.get("subPhase").getAsString());
        JsonArray players = message.get("players").getAsJsonArray();
        JsonObject gameBoard = message.get("gameBoard").getAsJsonObject();
        GameModel model = new GameModel(players.size(), round, phase, subphase, expert, activeUser, players, gameBoard);
        this.client.initializeGameModel(model);
    }

    /**
     * Manages the "error" message, exiting the current game if needed.
     *
     * @param message The message.
     */
    private void manageError(JsonObject message) {
        if (this.client.getClientState().equals(ClientState.GAME_RUNNING) && message.get("message").getAsString().equals("UserDisconnected")) {
            this.client.setClientState(ClientState.GAME_WAITING_ROOM);
            this.client.initializeGameModel(null);
        }
    }

    /**
     * Manages the "turnEnable" message, carrying the communication token.
     *
     * @param message The message.
     */
    private void manageTurnEnable(JsonObject message) {
        Log.debug("Token arrived.");
        synchronized (this.client.getLock()) {
            this.client.getGameModel().setCurrentPlayer(message.get("player").getAsString(), message.get("enable").getAsBoolean());
            this.client.getLock().notifyAll();
        }
    }

    /**
     * Manages the "win" message, setting the endState attribute of the clientController to the correct state.
     *
     * @param message The message.
     */
    private void manageEndGame(JsonObject message) {
        JsonArray winners;
        synchronized (this.client.getLock()) {
            if (message.get("winners") instanceof JsonNull) this.client.setEndState(EndType.DRAW);
            else {
                winners = message.get("winners").getAsJsonArray();
                for (JsonElement player : winners) {
                    if (player.getAsString().equals(this.client.getUserName())) {
                        this.client.setEndState(EndType.WIN);
                        break;
                    }
                }
                if (this.client.getEndState() == null) this.client.setEndState(EndType.LOSE);
            }
            this.client.setReplyArrived();
            this.client.getLock().notifyAll();
        }
        this.client.setClientState(ClientState.END_GAME);
    }

    /**
     * Returns true if the connection to the server is still alive.
     *
     * @return The boolean second stored into the connected attribute.
     */
    public boolean isConnected() {
        return this.connected;
    }

    /**
     * Sets the connection attribute to the given connection status.
     *
     * @param connectionStatus The connection status to set.
     */
    public void setConnected(boolean connectionStatus) {
        this.connected = connectionStatus;
    }

    /**
     * Gets a message from the server and converts it into a JsonObject.
     *
     * @return The JsonObject containing the incoming message.
     * @throws IOException Thrown if an error occurs during the message extraction from the input stream.
     */
    public JsonObject getMessage() throws IOException {
        synchronized (this.inputStream) {
            try {
                return JsonParser.parseString(this.inputStream.readLine()).getAsJsonObject();
            } catch (Exception e) {
                throw new IOException(e);
            }
        }
    }

    /**
     * Sends a command to the server, converting a JsonObject, containing the message, into a String.
     *
     * @param command The command to send.
     */
    public void sendCommand(JsonObject command) {
        synchronized (this.outputStream) {
            switch (this.client.getClientState()) {
                case GAME_CREATION, GAME_LOGIN, JOIN_GAME, GAME_RUNNING -> {
                    this.outputStream.println(command.toString());
                    outputStream.flush();
                }
                case GAME_WAITING_ROOM -> {
                    if (command.get("type").getAsString().equals("logout") || command.get("type").getAsString().equals("pong"))
                        this.outputStream.println(command);
                    outputStream.flush();
                }
                default -> {
                    if (command.get("type").getAsString().equals("pong")) {
                        this.outputStream.println(command);
                        outputStream.flush();
                    }
                }
            }
        }
    }

    /**
     * Sets the connection status to false and stops the ping thread.
     */
    public void disconnected() {
        synchronized (this.connectedLock) {
            setConnected(false);
        }
        this.ping.stopPing();
    }
}
