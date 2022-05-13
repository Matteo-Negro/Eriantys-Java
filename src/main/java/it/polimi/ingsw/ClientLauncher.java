package it.polimi.ingsw;

import it.polimi.ingsw.network.client.ClientCli;
import it.polimi.ingsw.utilities.GraphicsType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientLauncher {

    public static void main(String[] args) {
        try {
            //new Client(parseArgument(args));
            try {
                if (parseArgument((args)).equals(GraphicsType.CLI)) {
                    new Thread(new ClientCli()).start();
                }
            } catch (IOException ioe) {
                System.err.println("An error occurred while creating the controller class.");
            }
            /*else{
                // create ClientGui
            }*/

        } catch (IllegalArgumentException e) {
            System.err.println("Accepted arguments: --cli, -c, --gui or -g.");
        }
    }

    /**
     * Processes the arguments to decide whether the required interface is CLI or GUI.
     *
     * @param args Arguments passed to the client.
     * @return The interface that has to be instanced.
     * @throws IllegalArgumentException If there is an error while parsing the argument.
     */
    private static GraphicsType parseArgument(String[] args) throws IllegalArgumentException {

        List<String> graphics = new ArrayList<>();
        graphics.add("--cli");
        graphics.add("-c");
        graphics.add("--gui");
        graphics.add("-g");

        if (args.length == 0)
            return GraphicsType.GUI;

        if (args.length > 1 || !graphics.contains(args[0]))
            throw new IllegalArgumentException();

        if (args[0].equals(graphics.get(0)) || args[0].equals(graphics.get(1)))
            return GraphicsType.CLI;

        return GraphicsType.GUI;
    }
}
