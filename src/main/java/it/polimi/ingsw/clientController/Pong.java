package it.polimi.ingsw.clientController;

import com.google.gson.JsonObject;

public class Pong extends Thread{

    private final GameServer host;
    private final Object lock;

    public Pong(GameServer host){
        this.host = host;
        this.lock = new Object();
    }

    public void run(){
        JsonObject pongMessage = new JsonObject();
        pongMessage.addProperty("type", "pong");

        while(true){
            synchronized(lock){
                this.host.sendCommand(pongMessage);
                try{
                    lock.wait(1000);
                }catch(InterruptedException ie){
                    this.interrupt();
                }
            }

        }
    }
}
