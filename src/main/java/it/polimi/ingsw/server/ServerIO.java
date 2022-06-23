package it.polimi.ingsw.server;

import it.polimi.ingsw.utilities.Log;

import java.util.Scanner;

/**
 * A utility class which handles server's IO for debug and general management.
 *
 * @author Riccardo Milici
 */
public class ServerIO implements Runnable {
    private final Scanner input;
    private final Server server;
    private boolean closed;

    /**
     * Default class constructor.
     *
     * @param server The Server instance to bind.
     */
    public ServerIO(Server server) {
        this.server = server;
        this.closed = false;
        this.input = new Scanner(System.in);
    }

    /**
     * The core thread's method which acquires commands from System input.
     */
    public void run() {
        while (!this.closed) {
            System.out.println("Send the 'close' command to end the process: ");
            String command = input.nextLine();
            manageCommand(command);
        }
    }

    /**
     * Manages the acquired command.
     *
     * @param command The command to manage.
     */
    private void manageCommand(String command) {
        if (command.equals("close")) {
            Log.debug("Shutting down server.");
            this.server.shutdown();
        }
    }

    /**
     * Sets the closed attribute to true, this indicates that this service has been closed.
     */
    public void close() {
        this.closed = true;
    }
}
