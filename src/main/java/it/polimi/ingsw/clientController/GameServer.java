package it.polimi.ingsw.clientController;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.clientController.status.GameStatus;
import it.polimi.ingsw.network.client.ClientCli;
import it.polimi.ingsw.utilities.ClientStates;
import it.polimi.ingsw.utilities.MessageCreator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import static it.polimi.ingsw.view.cli.Utilities.printError;

public class GameServer extends Thread {

    private final BufferedReader inputStream;
    private final PrintWriter outputStream;
    private final ClientCli client;
    private final Ping ping;
    private boolean connected;
    private final Object connectedLock;

    public GameServer(Socket hostSocket, ClientCli client) throws IOException {
        this.inputStream = new BufferedReader(new InputStreamReader(hostSocket.getInputStream()));
        this.outputStream = new PrintWriter(hostSocket.getOutputStream());
        this.client = client;
        this.ping = new Ping(this);
        this.connected = true;
        this.connectedLock = new Object();

        this.ping.start();
        hostSocket.setSoTimeout(10000);
        //System.out.println("\nGameServer instance created");
    }

    public void run() {
        JsonObject incomingMessage;

        while(true){

            synchronized (connectedLock) {
                if (!connected)
                    break;
            }

            try {
                incomingMessage = getMessage();
                System.out.println(incomingMessage.get("type").getAsString());
                manageMessage(incomingMessage);
            } catch (IOException ioe) {
                this.disconnected();
            }
        }
    }

    private void manageMessage(JsonObject incomingMessage){
        //System.out.println("incoming message ");
        switch(incomingMessage.get("type").getAsString()){
            case "ping" ->{
                //System.out.println("ping arrived");
            }
            case "gameCreation" -> {
                System.out.println("gameCreation reply");
                sendCommand(MessageCreator.enterGame(incomingMessage.get("code").getAsString()));
            }
            case "enterGame" -> {
                System.out.println("enterGame reply");
                manageEnterGame(incomingMessage);
            }
            case "login" -> {
                System.out.println("login reply");
                manageLogin(incomingMessage);
            }
            //TODO modify turnEnable message with current player's name
            case "turnEnable" -> {}//manage turn enable
            case "command" ->{} //manage command

        }
    }

    private void manageEnterGame(JsonObject message){
        if(this.client.getClientState().equals(ClientStates.GAME_CREATION) || this.client.getClientState().equals(ClientStates.JOIN_GAME)) {
            if (message.get("found").getAsBoolean()) {
                Map<String, String> waitingRoom = new HashMap<>();
                int expectedPlayers = message.get("expectedPlayers").getAsInt();
                JsonArray players = message.get("players").getAsJsonArray();

                for (int i = 0; i < expectedPlayers; i++) {

                    String player = players.get(i).getAsJsonObject().get("name").getAsString();
                    boolean online = players.get(i).getAsJsonObject().get("online").getAsBoolean();
                    if (online) waitingRoom.put(player, "online");
                    else waitingRoom.put(player, "offline");

                }
                GameStatus newGameStatus = new GameStatus(expectedPlayers, waitingRoom);

                this.client.setClientState(ClientStates.GAME_LOGIN);
            }
            this.notify();
        }
        else disconnected();
    }

    private void manageLogin(JsonObject message){
        if(this.client.getClientState().equals(ClientStates.GAME_LOGIN)) {
            if (message.get("success").getAsBoolean()) {
                this.client.setClientState(ClientStates.GAME_WAITINGROOM);

            }
            this.client.getServerReplyLock().notify();
        }
        else disconnected();
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

        synchronized (this.connectedLock){
            setConnected(false);
        }
        this.ping.stopPing();
        this.client.setClientState(ClientStates.CONNECTION_LOST);
    }

}
