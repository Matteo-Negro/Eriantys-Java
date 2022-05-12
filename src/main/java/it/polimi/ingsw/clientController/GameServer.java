package it.polimi.ingsw.clientController;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GameServer {

    private BufferedReader inputStream;
    private PrintWriter outputStream;
    private ClientController client;
    private Pong pong;
    private boolean connected;

    public GameServer(Socket hostSocket, ClientController client) throws IOException {
        this.inputStream = new BufferedReader(new InputStreamReader(hostSocket.getInputStream()));
        this.outputStream = new PrintWriter(hostSocket.getOutputStream());
        this.client = client;
        this.pong = new Pong(this);
        this.connected = true;
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

    public void run(){
        JsonObject incomingMessage;
        while(true){
            if(!this.isConnected()) break;

            try{
                incomingMessage = this.getMessage();
                if(!incomingMessage.get("type").getAsString().equals("ping")) manageMessage(incomingMessage);
            }catch(IOException ioe){
                disconnected();
            }
        }
    }
    
    private void manageMessage(JsonObject message){

            switch (message.get("type").getAsString()){
                //TODO manage different server messages.
            }

    }

    private void disconnected(){
        setConnected(false);
        //TODO manage server disconnection into the ClientController
    }

}
