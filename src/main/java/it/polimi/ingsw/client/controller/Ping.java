package it.polimi.ingsw.client.controller;

import com.google.gson.JsonObject;

public class Ping extends Thread {

    private final GameServer host;
    private final Object lock;
    private boolean stop;

    public Ping(GameServer host) {
        this.host = host;
        this.lock = new Object();
        this.stop = false;

        //System.out.println("\nPong instance created");
    }

    public void run() {
        JsonObject pongMessage = new JsonObject();
        pongMessage.addProperty("type", "pong");
        //System.out.println("\nPong running");
        while (!stop) {
            synchronized (lock) {
                //System.out.println("\nPong sent");
                this.host.sendCommand(pongMessage);
                try {
                    lock.wait(1000);
                } catch (InterruptedException ie) {
                    this.stopPing();
                }
            }

        }
    }

    public void stopPing() {
        this.stop = true;
    }
}
