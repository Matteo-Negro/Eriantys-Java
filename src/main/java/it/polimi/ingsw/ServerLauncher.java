package it.polimi.ingsw;

import it.polimi.ingsw.network.server.Server;

import java.io.IOException;

public class ServerLauncher {

    public static void main(String[] args) {
        try {
            Server server = new Server(args.length == 1 ? args[0] : null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
