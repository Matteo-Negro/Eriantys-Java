package it.polimi.ingsw.client.view.gui.scenes;

import it.polimi.ingsw.client.view.ClientGui;
import it.polimi.ingsw.client.view.gui.utilities.EventProcessing;
import it.polimi.ingsw.client.view.gui.utilities.Images;
import it.polimi.ingsw.utilities.ClientState;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

/**
 * GUI scene for showing the end of a game.
 */
public class End implements Prepare {

    private ClientGui client;

    @FXML
    private Button exit;
    @FXML
    private ImageView result;

    /**
     * Initializes the scene.
     */
    public void initialize() {
        client = ClientGui.getInstance();
        ClientGui.link(ClientState.END_GAME, this);
    }

    /**
     * Prepares the scene for displaying.
     */
    @Override
    public void prepare() {
        Platform.runLater(() -> {
            exit.requestFocus();
            result.setImage(Images.getEndTitleByState(client.getController().getEndState()));
        });
    }

    /**
     * Goes back to main menu.
     *
     * @param event The event that triggered the function.
     */
    @FXML
    private void exit(Event event) {
        EventProcessing.exit(event, client);
    }
}
