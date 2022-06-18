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
public class Game implements Runnable {
    private final ClientGui client;

    /**
     * Default class constructor.
     *
     * @param client The client that's currently running.
     */
    public Game(ClientGui client) {
        this.client = client;
    }

    /**
     * The core class method, which waits for a game model update before updating the view.
     */
    @Override
    public void run() {
        synchronized (this.client.getController().getLock()) {
            while (client.getController().getClientState().equals(ClientState.GAME_RUNNING)) {
                try {
                    this.client.getController().getLock().wait();
                } catch (InterruptedException ie) {
                    Log.debug("Game update thread has been interrupted.");
                    Thread.currentThread().interrupt();
                }
                Platform.runLater(client::changeScene);
                Log.debug("gui updated");
            }

        }
    }

    public void stop() {
        Thread.currentThread().interrupt();
    }
}
