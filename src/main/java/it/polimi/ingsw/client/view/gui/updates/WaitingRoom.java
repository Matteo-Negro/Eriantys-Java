package it.polimi.ingsw.client.view.gui.updates;

import it.polimi.ingsw.client.view.ClientGui;
import it.polimi.ingsw.utilities.ClientState;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Updates the WaitingRoom.
 *
 * @author Riccardo Milici
 * @author Riccardo Motta
 */
public class WaitingRoom implements Runnable {

    private final ClientGui client;
    private final it.polimi.ingsw.client.view.gui.scenes.WaitingRoom waitingRoomGUI;

    /**
     * Default constructor.
     *
     * @param client         The current ClientGUI instance.
     * @param waitingRoomGUI The current WaitingRoom (GUI) instance.
     */
    public WaitingRoom(ClientGui client, it.polimi.ingsw.client.view.gui.scenes.WaitingRoom waitingRoomGUI) {
        this.client = client;
        this.waitingRoomGUI = waitingRoomGUI;
    }

    /**
     * The core class method, which periodically updates the view.
     */
    @Override
    public void run() {
        while (client.getController().getClientState().equals(ClientState.GAME_WAITING_ROOM)) {
            synchronized (this.client.getController().getLock()) {
                if (client.getController().getGameModel() != null)
                    this.waitingRoomGUI.update(getPlayers());
                try {
                    this.client.getController().getLock().wait(1000);
                } catch (InterruptedException e) {
                    client.getController().resetGame();
                    Thread.currentThread().interrupt();
                }
            }
        }
        Platform.runLater(client::changeScene);
    }

    /**
     * Gets the labels containing players' names.
     *
     * @return List of Labels containing players' names.
     */
    private List<Label> getPlayers() {

        List<Label> players = new ArrayList<>();
        List<String> sortedPlayers = client.getController().getGameModel().getWaitingRoom().entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .sorted()
                .toList();
        int index = sortedPlayers.size();

        for (String player : sortedPlayers) {
            Label label = new Label(player);
            if (index != 1)
                label.setOpaqueInsets(new Insets(0, 0, 10, 0));
            label.setTextFill(Color.rgb(255, 255, 255));
            index--;
            players.add(label);
        }

        return players;
    }

    /**
     * Stops the current thread.
     */
    public void stop() {
        Thread.currentThread().interrupt();
    }
}
