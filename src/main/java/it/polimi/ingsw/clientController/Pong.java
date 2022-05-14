package it.polimi.ingsw.clientController;

import com.google.gson.JsonObject;

public class Pong extends Thread {

    private final GameServer host;
    private final Object lock;

    public Pong(GameServer host) {
        this.host = host;
        this.lock = new Object();

        //System.out.println("\nPong instance created");
    }

    public void run() {
        JsonObject pongMessage = new JsonObject();
        pongMessage.addProperty("type", "pong");
        //System.out.println("\nPong running");
        while (true) {
            synchronized (lock) {
                //System.out.println("\nPong sent");
                this.host.sendCommand(pongMessage);
                try {
                    lock.wait(1000);
                } catch (InterruptedException ie) {
                    this.interrupt();
                }
            }

        }
    }
}
