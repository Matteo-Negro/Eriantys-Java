package it.polimi.ingsw.client.controller;

import com.google.gson.JsonObject;
import it.polimi.ingsw.utilities.Log;

public class Ping extends Thread {

    private final GameServer host;
    private final Object lock;
    private boolean stop;

    public Ping(GameServer host) {
        this.host = host;
        this.lock = new Object();
        this.stop = false;

        Log.info("Ping instance created");
    }

    @Override
    public void run() {
        JsonObject pongMessage = new JsonObject();
        pongMessage.addProperty("type", "pong");
        //Log.info("Pong running");
        while (!stop && this.host.isConnected()) {
            synchronized (lock) {
                //Log.debug("Pong sent");
                this.host.sendCommand(pongMessage);
                try {
                    lock.wait(1000);
                } catch (InterruptedException e) {
                    this.stopPing();
                }
            }

        }
    }

    public void stopPing() {
        synchronized (this.lock) {
            this.stop = true;
        }
    }
}
