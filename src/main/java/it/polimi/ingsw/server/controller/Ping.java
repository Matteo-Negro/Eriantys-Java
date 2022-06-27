package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.utilities.Log;
import it.polimi.ingsw.utilities.MessageCreator;

/**
 * This is the ping class for the server, it runs a parallel thread which sends a periodical signal
 * to the client, in order to keep the connection alive.
 *
 * @author Riccardo Milici
 * @author Riccardo Motta
 * @author Matteo Negro
 */
public class Ping implements Runnable {

    private final User user;
    private final Object lock;
    private boolean inWaitingRoom;
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
        Log.info("Ping running");
        while (!stop) {
            synchronized (lock) {
                user.sendMessage(MessageCreator.ping());
                if (inWaitingRoom) {
                    user.sendMessage(MessageCreator.waitingRoomUpdate(user.getGameController()));
                }

                try {
                    lock.wait(1000);
                } catch (InterruptedException e) {
                    this.stopPing();
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    /**
     * It stops the ping thread.
     */
    public void stopPing() {
        Log.debug("Ping stopped.");
        this.stop = true;
    }

    /**
     * Returns true if the user is in a waiting-room.
     *
     * @return The inWaitingRoom attribute.
     */
    public boolean isInWaitingRoom() {
        return this.inWaitingRoom;
    }

    /**
     * Sets the inWaitingRoom attribute to true when the user is in a waiting-room.
     *
     * @param status The boolean second to set.
     */
    public void setInWaitingRoom(boolean status) {
        this.inWaitingRoom = status;
    }
}
