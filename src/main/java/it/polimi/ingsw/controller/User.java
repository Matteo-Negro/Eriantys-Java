package it.polimi.ingsw.controller;

import java.net.Socket;
import java.util.Map;

public class User{

    private int id;
    private Socket socket;
    private String name;

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
