package it.polimi.ingsw.client.controller;

import it.polimi.ingsw.utilities.Log;
import it.polimi.ingsw.utilities.MessageCreator;

/**
 * This is the ping class for the client, it runs a parallel thread which sends a periodical signal
 * to the server, in order to keep the connection alive.
 *
 * @author Riccardo Milici
 * @author Riccardo Motta
 * @author Matteo Negro
 */
public class Ping implements Runnable {

    private final GameServer host;
    private final Object lock;
    private boolean stop;

    /**
     * Default class constructor.
     *
     * @param host The GameServer instance which instantiated this class.
     */
    public Ping(GameServer host) {
        this.host = host;
        this.lock = new Object();
        this.stop = false;

        Log.info("Ping instance created");
    }

    /**
     * The main ping method, sends a "pong" message periodically.
     */
    @Override
    public void run() {
        while (!stop && this.host.isConnected()) {
            synchronized (lock) {
                this.host.sendCommand(MessageCreator.pong());
                try {
                    lock.wait(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    this.stopPing();
                }
            }
        }
    }

    /**
     * Stops the ping thread, setting the stop attribute to true.
     */
    public void stopPing() {
        synchronized (this.lock) {
            this.stop = true;
        }
    }
}
