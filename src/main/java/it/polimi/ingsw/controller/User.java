package it.polimi.ingsw.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class User {

    private int id;
    private final Socket socket;
    private String name;
    private boolean isConnected;
    private ObjectInputStream socketInputStream;
    private ObjectOutputStream socketOutputStream;

    public User(Socket socket) throws IOException{
        this.id = -1;
        this.socket = socket;
        this.name = null;
        this.isConnected = false;

        socketInputStream = new ObjectInputStream(socket.getInputStream());
        socketOutputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    public void putName(String username) {
        this.name = username;
    }

    public void putId(int userId) {
        this.id = userId;
    }

    public void setIsConnected(boolean connectionStatus) {
        this.isConnected = connectionStatus;
    }

    public int getId() {
        return id;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getName() {
        return name;
    }

    public boolean getIsConnected() {
        return isConnected;
    }

    public JsonObject getCommand() throws IOException, ClassNotFoundException{

        String message = (String) socketInputStream.readObject();
        return JsonParser.parseString(message).getAsJsonObject();
    }

    public void sendCommand(JsonObject command) throws IOException{

        String message = command.getAsString();
        socketOutputStream.writeObject(message);
    }
}
