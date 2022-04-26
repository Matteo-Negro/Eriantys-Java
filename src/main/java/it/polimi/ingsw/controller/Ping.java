package it.polimi.ingsw.controller;

import com.google.gson.JsonObject;

public class Ping extends Thread {

    private final User user;

    public Ping(User user) {
        this.user = user;
    }

    @Override
    public void run() {
        JsonObject ping = new JsonObject();
        ping.addProperty("type", "ping");

        while (true) {
            synchronized (this) {
                user.sendMessage(ping);
            }
            try {
                this.wait(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
