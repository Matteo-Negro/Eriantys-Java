package it.polimi.ingsw.controller;

import java.net.Socket;

public class User {

    private final int id;
    private final Socket socket;
    private final String name;

    public User(int id, Socket socket, String name) {
        this.id = id;
        this.socket = socket;
        this.name = name;
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

//    public Map<> getCommand(){
//
//    }
}
