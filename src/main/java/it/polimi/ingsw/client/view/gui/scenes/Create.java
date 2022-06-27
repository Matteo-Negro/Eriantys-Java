package it.polimi.ingsw.client.view.gui.scenes;

import it.polimi.ingsw.client.view.ClientGui;
import it.polimi.ingsw.client.view.gui.utilities.EventProcessing;
import it.polimi.ingsw.utilities.ClientState;
import it.polimi.ingsw.utilities.MessageCreator;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * GUI scene for creating a new game.
 */
public class Create implements Prepare {

    private final AtomicInteger players;
    private final AtomicBoolean expertMode;
    private ClientGui client;
    @FXML
    private Button create;
    @FXML
    private RadioButton expert;
    @FXML
    private RadioButton normal;
    @FXML
    private RadioButton players2;
    @FXML
    private RadioButton players3;
    @FXML
    private RadioButton players4;

    /**
     * Default constructor.
     */
    public Create() {
        players = new AtomicInteger(2);
        expertMode = new AtomicBoolean(false);
    }

    /**
     * Initializes the scene.
     */
    public void initialize() {
        client = ClientGui.getInstance();
        ClientGui.link(ClientState.GAME_CREATION, this);
    }

    /**
     * Prepares the scene for displaying.
     */
    @Override
    public void prepare() {
        Platform.runLater(() -> {
            create.requestFocus();
            players2.setSelected(true);
            normal.setSelected(true);
            expert.setSelected(false);
            players3.setSelected(false);
            players4.setSelected(false);
        });
    }

    /**
     * Goes back to main menu.
     *
     * @param event The event that triggered the function.
     */
    @FXML
    private void back(Event event) {
        EventProcessing.exit(event, client);
    }

    /**
     * Creates a new game.
     *
     * @param event The event that triggered the function.
     */
    @FXML
    private void create(Event event) {
        if (EventProcessing.standard(event) && !client.getController().getClientState().equals(ClientState.CONNECTION_LOST))
            client.getController().manageGameCreation(MessageCreator.gameCreation(players.get(), expertMode.get()));
    }

    /**
     * Sets the expert mode.
     *
     * @param event The event that triggered the function.
     */
    @FXML
    private void expert(Event event) {
        EventProcessing.standard(event);
        expertMode.set(true);
        Platform.runLater(() -> {
            expert.setSelected(true);
            normal.setSelected(false);
        });
    }

    /**
     * Sets the normal mode.
     *
     * @param event The event that triggered the function.
     */
    @FXML
    private void normal(Event event) {
        EventProcessing.standard(event);
        expertMode.set(false);
        Platform.runLater(() -> {
            expert.setSelected(false);
            normal.setSelected(true);
        });
    }

    /**
     * Sets the number of players to 2.
     *
     * @param event The event that triggered the function.
     */
    @FXML
    private void players2(Event event) {
        EventProcessing.standard(event);
        players.set(2);
        Platform.runLater(() -> {
            players2.setSelected(true);
            players3.setSelected(false);
            players4.setSelected(false);
        });
    }

    /**
     * Sets the number of players to 3.
     *
     * @param event The event that triggered the function.
     */
    @FXML
    private void players3(Event event) {
        EventProcessing.standard(event);
        players.set(3);
        Platform.runLater(() -> {
            players2.setSelected(false);
            players3.setSelected(true);
            players4.setSelected(false);
        });
    }

    /**
     * Sets the number of players to 4.
     *
     * @param event The event that triggered the function.
     */
    @FXML
    private void players4(Event event) {
        EventProcessing.standard(event);
        players.set(4);
        Platform.runLater(() -> {
            players2.setSelected(false);
            players3.setSelected(false);
            players4.setSelected(true);
        });
    }
}
