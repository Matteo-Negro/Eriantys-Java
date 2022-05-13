package it.polimi.ingsw.clientController;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import it.polimi.ingsw.network.client.ClientCli;
import it.polimi.ingsw.utilities.ClientStates;

public class GameServer extends Thread{

    private final BufferedReader inputStream;
    private final PrintWriter outputStream;
    private final ClientCli client;
    private final Pong pong;
    private boolean connected;

    public GameServer(Socket hostSocket, ClientCli client) throws IOException {
        this.inputStream = new BufferedReader(new InputStreamReader(hostSocket.getInputStream()));
        this.outputStream = new PrintWriter(hostSocket.getOutputStream());
        this.client = client;
        this.pong = new Pong(this);
        this.connected = true;

        pong.start();
        System.out.println("\nGameServer instance created");
    }

    public void run(){
        try{
            JsonObject incomingMessage = getMessage();
            if(!incomingMessage.get("type").getAsString().equals("ping")) client.manageMessage(incomingMessage);
        }catch(IOException ioe){
            //Connection to the server lost.
            setConnected(false);
        }
    }

    public boolean isConnected(){
        return this.connected;
    }

    public void setConnected(boolean connectionStatus){
        this.connected = connectionStatus;
    }

    public JsonObject getMessage() throws IOException{
        return JsonParser.parseString(this.inputStream.readLine()).getAsJsonObject();
    }

    public void sendCommand(JsonObject command){
        this.outputStream.println(command.toString());
        outputStream.flush();
    }

    private void disconnected(){
        setConnected(false);
        this.client.setClientState(ClientStates.CONNECTION_LOST);
    }

}
