package it.polimi.ingsw.controller;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class User {

    private int id;
    private Socket socket;
    private String name;
    private ObjectInputStream socketInputStream;

    public User(Socket socket) throws IOException{
        this.id = -1;
        this.socket = socket;
        this.name = null;

        socketInputStream = new ObjectInputStream(socket.getInputStream());
    }

    public void putName(String username) {
        this.name = username;
    }

    public void putId(int userId) {
        this.id = userId;
    }

    public void putSocket(Socket userSocket) throws IOException{
        this.socket = userSocket;

        socketInputStream = new ObjectInputStream(socket.getInputStream());
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

    public JsonObject getCommand() throws IOException, ClassNotFoundException{
        return (JsonObject) socketInputStream.readObject();
    }
}
