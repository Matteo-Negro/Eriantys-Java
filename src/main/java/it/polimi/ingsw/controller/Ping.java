package it.polimi.ingsw.controller;

import com.google.gson.JsonObject;

public class Ping extends Thread {

    private final User user;
    private final Object lock;

    /**
     * Default constructor.
     *
     * @param user The user to ping.
     */
    public Ping(User user) {
        this.user = user;
        this.lock = new Object();
    }

    /**
     * Periodically generates a ping message and sends it to the user.
     */
    @Override
    public void run() {
        JsonObject ping = new JsonObject();
        ping.addProperty("type", "ping");

        while (true) {
            synchronized (lock) {
                user.sendMessage(ping);
                try {
                    lock.wait(1000);
                } catch (InterruptedException e) {
                    this.interrupt();
                }
            }
        }
    }
}
