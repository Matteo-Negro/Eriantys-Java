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
    private final it.polimi.ingsw.client.view.gui.scenes.Game gameGui;

    /**
     * Default class constructor.
     *
     * @param client  The client that's currently running.
     * @param gameGui The Game instance to update.
     */
    public Game(ClientGui client, it.polimi.ingsw.client.view.gui.scenes.Game gameGui) {
        this.client = client;
        this.gameGui = gameGui;
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
                gameGui.update();
                Log.debug("gui updated");
            }
            Platform.runLater(client::changeScene);
        }
    }

    /**
     * Stops the current thread.
     */
    public void stop() {
        Thread.currentThread().interrupt();
    }
}
