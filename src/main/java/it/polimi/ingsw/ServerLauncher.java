package it.polimi.ingsw;

import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.utilities.Log;
import it.polimi.ingsw.utilities.Pair;

import java.io.IOException;

/**
 * The server launcher.
 * It instantiates the core classes needed in order to run the server.
 *
 * @author Riccardo Motta.
 */
public class ServerLauncher {

    public static void main(String[] args) {

        try {
            Log.createServerInstance();
            Log.setLevel(Log.Level.DEBUG);
        } catch (IOException | IllegalAccessException e) {
            System.exit(1);
        }

        Pair<String, Integer> arguments = parseArguments(args);

        try {
            new Server(arguments.key(), arguments.value()).start();
        } catch (IOException e) {
            Log.error("Shutdown server.");
        }
    }

    /**
     * This method analyzes the input arguments.
     *
     * @param args The arguments to analyze.
     * @return A pair containing the save path for the database and the TCP port for the server socket.
     */
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
