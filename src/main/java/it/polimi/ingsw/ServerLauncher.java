package it.polimi.ingsw;

import it.polimi.ingsw.network.server.Server;

public class ServerLauncher {

    public static void main(String[] args) {
        Server server = new Server(args.length == 1 ? args[0] : null);
    }

}
