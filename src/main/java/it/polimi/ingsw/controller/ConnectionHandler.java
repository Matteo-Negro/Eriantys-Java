package it.polimi.ingsw.controller;

import it.polimi.ingsw.ServerLauncher;

import java.net.Socket;

public class ConnectionHandler {

    private Socket userSocket;
    private ServerLauncher gameServer;

    public ConnectionHandler(Socket userSocket, ServerLauncher gameServer) {
        this.userSocket = userSocket;
        this.gameServer = gameServer;
    }

    public void createGame() {

    }
}
