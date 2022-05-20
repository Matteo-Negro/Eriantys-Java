package it.polimi.ingsw.client.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.client.ClientCli;
import it.polimi.ingsw.client.model.GameModel;
import it.polimi.ingsw.utilities.ClientStates;
import it.polimi.ingsw.utilities.GameControllerStates;
import it.polimi.ingsw.utilities.Log;
import it.polimi.ingsw.utilities.MessageCreator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class GameServer extends Thread {

    private final BufferedReader inputStream;
    private final PrintWriter outputStream;
    private final ClientCli client;
    private final Object connectedLock;
    private boolean connected;
    private final Ping ping;

    public GameServer(Socket hostSocket, ClientCli client) throws IOException {
        this.inputStream = new BufferedReader(new InputStreamReader(hostSocket.getInputStream()));
        this.outputStream = new PrintWriter(hostSocket.getOutputStream());
        this.client = client;
        this.connected = true;
        this.connectedLock = new Object();
        this.ping = new Ping(this);
        Log.info("GameServer instance created");
    }

    public void run() {
        new Thread(ping).start();
        JsonObject incomingMessage;

        while (true) {

            synchronized (connectedLock) {
                if (!connected)
                    break;
            }

            try {
                incomingMessage = getMessage();
                //Log.debug(incomingMessage.get("type").getAsString());
                manageMessage(incomingMessage);
            } catch (IOException ioe) {
                this.client.setClientState(ClientStates.CONNECTION_LOST);
            }
        }
    }

    private void manageMessage(JsonObject incomingMessage) {
        //Log.debug("incoming message ");
        switch (incomingMessage.get("type").getAsString()) {
            case "ping" -> {
                //Log.debug("ping arrived");
            }
            case "gameCreation" -> {
                Log.debug("gameCreation reply");
                sendCommand(MessageCreator.enterGame(incomingMessage.get("code").getAsString()));
                this.client.setGameCode(incomingMessage.get("code").getAsString());
            }
            case "enterGame" -> {
                Log.debug("enterGame reply");
                manageEnterGame(incomingMessage);
            }
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
                this.client.setClientState(ClientStates.GAME_RUNNING);
            }

            case "playAssistant" -> {
            }

            case "moveStudent" -> {
            }

            case "payCharacter" -> {
            }

            case "moveProfessor" -> {
            }

            case "moveTower" -> {
            }

            case "endGame" -> {
            }

            case "turnEnable", "command" -> {
            }//manage turn enable
            //manage command
        }
    }

    private void manageEnterGame(JsonObject message) {

        switch (this.client.getClientState()) {
            case GAME_CREATION, JOIN_GAME -> {
                if (message.get("found").getAsBoolean()) {
                    parseEnterGame(message);
                    this.client.setClientState(ClientStates.GAME_LOGIN);
                } else {
                    this.client.errorOccurred("This game is full.");
                }
            }
            case GAME_WAITING_ROOM -> parseEnterGame(message);
            default -> this.client.setClientState(ClientStates.CONNECTION_LOST);
        }

        synchronized (this.client.getLock()) {
            this.client.getLock().notify();
        }
    }

    private void manageLogin(JsonObject message) {
        if (this.client.getClientState().equals(ClientStates.GAME_LOGIN)) {
            if (message.get("success").getAsBoolean()) {
                this.client.setClientState(ClientStates.GAME_WAITING_ROOM);
            } else {
                this.client.setClientState(ClientStates.CONNECTION_LOST);
            }
        } else this.client.setClientState(ClientStates.CONNECTION_LOST);

        synchronized (this.client.getLock()) {
            this.client.getLock().notify();
        }
    }

    private void manageStatus(JsonObject incomingMessage) {

        int round = incomingMessage.get("round").getAsInt();
        String activeUser = null;
        if (!(incomingMessage.get("activeUser") instanceof JsonNull))
            activeUser = incomingMessage.get("activeUser").getAsString();
        boolean expert = incomingMessage.get("expert").getAsBoolean();
        String phase = incomingMessage.get("phase").getAsString();
        GameControllerStates subphase = GameControllerStates.valueOf(incomingMessage.get("subPhase").getAsString());
        JsonArray players = incomingMessage.get("players").getAsJsonArray();
        JsonObject gameBoard = incomingMessage.get("gameBoard").getAsJsonObject();
        Log.debug("sono dentro 0");
        GameModel model = new GameModel(players.size(), round, phase, subphase, expert, activeUser, players, gameBoard);
        Log.debug("sono dentro 1");
        this.client.initializeGameModel(model);
    }

    public boolean isConnected() {
        return this.connected;
    }

    public void setConnected(boolean connectionStatus) {
        this.connected = connectionStatus;
    }

    public JsonObject getMessage() throws IOException {

        return JsonParser.parseString(this.inputStream.readLine()).getAsJsonObject();

    }

    public void sendCommand(JsonObject command) {
        synchronized (this.outputStream) {
            this.outputStream.println(command.toString());
            outputStream.flush();
        }
    }

    public void disconnected() {

        synchronized (this.connectedLock) {
            setConnected(false);
        }
        this.ping.stopPing();
    }

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

}
