package it.polimi.ingsw.server.controller;

import com.google.gson.JsonObject;
import it.polimi.ingsw.utilities.Log;

public class Ping extends Thread {

    private final User user;
    private final Object lock;
    private boolean stop;

    /**
     * Default constructor.
     *
     * @param user The user to ping.
     */
    public Ping(User user) {
        this.user = user;
        this.lock = new Object();
        this.stop = false;
        Log.info("Ping instance created");
    }

    /**
     * Periodically generates a ping message and sends it to the user.
     */
    @Override
    public void run() {
        JsonObject ping = new JsonObject();
        ping.addProperty("type", "ping");
        Log.info("Ping running");
        while (!stop) {
            synchronized (lock) {
                user.sendMessage(ping);
                Log.debug("Ping");
                try {
                    lock.wait(1000);
                } catch (InterruptedException e) {
                    this.stopPing();
                }
            }
        }
    }

    public void stopPing() {
        this.stop = true;
    }
}
