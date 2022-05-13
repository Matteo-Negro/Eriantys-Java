package it.polimi.ingsw;

import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.utilities.Pair;

import java.io.IOException;

public class ServerLauncher {

    public static void main(String[] args) {

        Pair<String, Integer> arguments = parseArguments(args);

        try {
            new Server(arguments.key(), arguments.value()).start();
        } catch (IOException e) {
            System.out.println("Shutdown server.");
        }
    }

    private static Pair<String, Integer> parseArguments(String[] args) {

        String path = null;
        int port = 36803;
        boolean foundPath = false;
        boolean foundPort = false;

        for (String arg : args) {
            if (foundPath)
                path = arg;
            else if (foundPort)
                port = Integer.parseInt(arg);
            if (arg.equals("-db") || arg.equals("--database")) {
                foundPath = true;
                foundPort = false;
            } else if (arg.equals("-p") || arg.equals("-- port")) {
                foundPort = true;
                foundPath = false;
            }
        }

        return new Pair<>(path, port);
    }
}
