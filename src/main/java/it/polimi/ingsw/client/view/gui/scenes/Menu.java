package it.polimi.ingsw.client.view.gui.scenes;

import it.polimi.ingsw.client.view.ClientGui;
import it.polimi.ingsw.client.view.gui.utilities.EventProcessing;
import it.polimi.ingsw.utilities.ClientState;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * GUI scene for showing the main menu.
 */
public class Menu implements Prepare {

    private ClientGui client;

    @FXML
    private Button enter;

    /**
     * Initializes the scene.
     */
    public void initialize() {
        client = ClientGui.getInstance();
        ClientGui.link(ClientState.MAIN_MENU, this);
    }

    /**
     * Prepares the scene for displaying.
     */
    @Override
    public void prepare() {
        Platform.runLater(() -> enter.requestFocus());
    }

    /**
     * Creates a new game.
     *
     * @param event The event that triggered the function.
     */
    @FXML
    private void create(Event event) {
        if (!EventProcessing.standard(event))
            return;
        client.getController().manageMainMenu("1");
    }

    /**
     * Enters a game.
     *
     * @param event The event that triggered the function.
     */
    @FXML
    private void enter(Event event) {
        if (!EventProcessing.standard(event))
            return;
        client.getController().manageMainMenu("2");
    }

    /**
     * Logs out from the server.
     */
    @FXML
    private void exit(Event event) {
        if (!EventProcessing.standard(event))
            return;
        client.getController().manageConnectionLost();
    }
}
