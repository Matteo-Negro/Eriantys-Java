package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.view.ClientGui;
import it.polimi.ingsw.utilities.ClientStates;
import it.polimi.ingsw.utilities.Log;
import it.polimi.ingsw.utilities.MessageCreator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class WaitingRoom {

    private static Scene scene = null;
    private static ClientGui client = null;

    @FXML
    private static Button back;
    @FXML
    private static Label code;
    @FXML
    private static VBox names;
    @FXML
    private static Label online;
    private static it.polimi.ingsw.client.view.gui.updates.WaitingRoom update;

    private WaitingRoom() {
    }

    /**
     * Initializes the scene.
     *
     * @param client The client to which change the state.
     * @throws IOException Thrown if there is an error somewhere.
     */
    public static void initialize(ClientGui client) throws IOException {
        if (client == null)
            return;
        it.polimi.ingsw.client.view.gui.WaitingRoom.client = client;
        scene = new Scene(FXMLLoader.load(Objects.requireNonNull(WaitingRoom.class.getResource("/fxml/waiting_room.fxml"))));
        lookup();
        addEvents();
        update = new it.polimi.ingsw.client.view.gui.updates.WaitingRoom(client);
    }

    /**
     * Returns the scene.
     *
     * @return The scene.
     */
    public static Scene getScene() {
        code.requestFocus();
        code.setText(client.getController().getGameCode());
        names.getChildren().clear();
        new Thread(update).start();
        return scene;
    }

    /**
     * Looks for every used element in the scene.
     */
    private static void lookup() {
        back = (Button) scene.lookup("#back");
        code = (Label) scene.lookup("#code");
        names = (VBox) scene.lookup("#names");
        online = (Label) scene.lookup("#online");
    }

    /**
     * Adds all the events to the scene.
     */
    private static void addEvents() {
        back.setOnMouseClicked(event -> {
            event.consume();
            disconnect();
        });

        back.setOnKeyPressed(event -> {
            event.consume();
            if (event.getCode() == KeyCode.ENTER)
                disconnect();
        });
    }

    private static void disconnect() {
        if(!client.getController().getClientState().equals(ClientStates.CONNECTION_LOST)){
            client.getController().getGameServer().sendCommand(MessageCreator.logout());
            client.getController().resetGame();
            client.getController().setClientState(ClientStates.MAIN_MENU);
        }
        client.changeScene();
    }

    public static void update(List<Label> players) {
        Platform.runLater(() -> {
            online.setText(String.format("%d / %d", players.size(), client.getController().getGameModel().getPlayersNumber()));
            names.getChildren().clear();
            names.getChildren().addAll(players);
        });
    }

    public static void changeScene() {
        Platform.runLater(() -> client.changeScene());
    }
}
