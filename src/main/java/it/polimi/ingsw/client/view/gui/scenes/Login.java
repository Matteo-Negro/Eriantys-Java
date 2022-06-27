package it.polimi.ingsw.client.view.gui.scenes;

import it.polimi.ingsw.client.view.ClientGui;
import it.polimi.ingsw.client.view.gui.utilities.EventProcessing;
import it.polimi.ingsw.utilities.ClientState;
import it.polimi.ingsw.utilities.Pair;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * GUI scene for logging into a game.
 */
public class Login implements Prepare {

    private ClientGui client;

    @FXML
    private TextField name;
    @FXML
    private VBox players;

    /**
     * Initializes the scene.
     */
    public void initialize() {
        client = ClientGui.getInstance();
        ClientGui.link(ClientState.GAME_LOGIN, this);
    }

    /**
     * Prepares the scene for displaying.
     */
    @Override
    public void prepare() {
        List<Label> labels = getPlayers();
        Platform.runLater(() -> {
            name.setText("");
            name.requestFocus();
            players.getChildren().clear();
            for (Label label : labels)
                players.getChildren().add(label);
        });
    }

    /**
     * Gets the players' list of the game.
     *
     * @return The players' list.
     */
    private List<Label> getPlayers() {
        List<Label> labels = new ArrayList<>();
        Map<String, Boolean> playersMap = client.getController().getGameModel().getWaitingRoom();
        List<Pair<String, Boolean>> sortedPlayers = playersMap.keySet().stream().sorted()
                .map(name -> new Pair<>(name, playersMap.get(name))).toList();
        int index = client.getController().getGameModel().getWaitingRoom().size();
        for (Pair<String, Boolean> entry : sortedPlayers) {
            Label label = new Label(entry.first());
            if (index != 1)
                label.setOpaqueInsets(new Insets(0, 0, 10, 0));
            if (Boolean.TRUE.equals(entry.second()))
                label.setTextFill(Color.rgb(181, 255, 181));
            else
                label.setTextFill(Color.rgb(255, 181, 181));
            index--;
            labels.add(label);
        }
        return labels;
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
     * Logs into the game.
     *
     * @param event The event that triggered the function.
     */
    @FXML
    private void login(Event event) {
        if (EventProcessing.standard(event))
            manageLogin();
    }

    /**
     * Logs into the game.
     *
     * @param event The event that triggered the function.
     */
    @FXML
    private void name(Event event) {
        if (EventProcessing.text(event))
            manageLogin();
    }

    /**
     * Manages the login to the game.
     */
    private void manageLogin() {
        if (!client.getController().getClientState().equals(ClientState.CONNECTION_LOST))
            client.getController().manageGameLogin(name.getText());
    }
}
