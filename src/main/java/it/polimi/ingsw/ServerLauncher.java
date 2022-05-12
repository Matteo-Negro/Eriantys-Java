package it.polimi.ingsw;

import it.polimi.ingsw.network.server.Server;

import java.io.IOException;

public class ServerLauncher {

    public static void main(String[] args) {
        try {
            new Server(args.length == 1 ? args[0] : null, 36803).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
