package it.polimi.ingsw.client.view.gui.scenes;

import it.polimi.ingsw.client.view.ClientGui;
import it.polimi.ingsw.client.view.gui.utilities.EventProcessing;
import it.polimi.ingsw.utilities.ClientState;
import it.polimi.ingsw.utilities.MessageCreator;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * This class manages the GUI update while in the waiting room.
 *
 * @author Riccardo Motta
 */
public class WaitingRoom implements Prepare {

    private ClientGui client;

    @FXML
    private Label code;
    @FXML
    private VBox names;
    @FXML
    private Label online;

    /**
     * Initializes the scene.
     */
    public void initialize() {
        client = ClientGui.getInstance();
        ClientGui.link(ClientState.GAME_WAITING_ROOM, this);
    }

    /**
     * Prepares the scene for displaying.
     */
    @Override
    public void prepare() {
        Platform.runLater(() -> {
            code.requestFocus();
            code.setText(client.getController().getGameCode());
        });
    }

    /**
     * Goes back to main menu.
     *
     * @param event The event that triggered the function.
     */
    @FXML
    private void back(Event event) {
        if (EventProcessing.standard(event) && !client.getController().getClientState().equals(ClientState.CONNECTION_LOST)) {
            client.getController().getGameServer().sendCommand(MessageCreator.logout());
            client.getController().resetGame();
            client.getController().setClientState(ClientState.MAIN_MENU);
        }
    }

    /**
     * Updates the view.
     *
     * @param players The players to display.
     */
    public void update(List<Label> players) {
        Platform.runLater(() -> {
            online.setText(String.format("%d / %d", players.size(), client.getController().getGameModel().getPlayersNumber()));
            names.getChildren().clear();
            names.getChildren().addAll(players);
        });
    }

    /**
     * Interrupts the method.
     */
    public void stop() {
        Thread.currentThread().interrupt();
    }
}
