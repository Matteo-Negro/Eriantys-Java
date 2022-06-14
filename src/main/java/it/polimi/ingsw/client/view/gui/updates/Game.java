package it.polimi.ingsw.client.view.gui.updates;

import it.polimi.ingsw.client.view.ClientGui;
import it.polimi.ingsw.utilities.ClientState;
import it.polimi.ingsw.utilities.Log;
import javafx.application.Platform;

/**
 * This class manages the GUI update while the game is running.
 *
 * @author Riccardo Milici.
 */
public class Game implements Runnable, Observer{
    private final ClientGui client;
    private final Object lock;

    /**
     * Default class constructor.
     *
     * @param client The client that's currently running.
     */
    public Game(ClientGui client) {
        this.client = client;
        this.lock = new Object();
        this.client.getModelObserver().attachObserver(this);
    }

    /**
     * The core class method, which waits for a game model update before updating the view.
     */
    @Override
    public void run() {
        while(client.getController().getClientState().equals(ClientState.GAME_RUNNING)) {
            synchronized (this.lock) {
                try {
                    this.lock.wait();
                } catch(InterruptedException ie) {
                    Log.debug("Game update thread has been interrupted.");
                    Thread.currentThread().interrupt();
                }
            }
            Platform.runLater(client::changeScene);
        }
    }

    /**
     * Wakes up the thread waiting on the "lock" attribute.
     */
    @Override
    public void notifyUpdate() {
        synchronized(this.lock) {
            this.lock.notifyAll();
        }
    }
 }
