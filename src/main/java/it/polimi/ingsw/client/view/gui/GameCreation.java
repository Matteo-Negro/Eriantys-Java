package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.view.ClientGui;
import it.polimi.ingsw.utilities.ClientStates;
import it.polimi.ingsw.utilities.MessageCreator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class GameCreation {

    private static Scene scene = null;
    private static ClientGui client = null;

    @FXML
    private static Button back;
    @FXML
    private static Button create;
    @FXML
    private static RadioButton expert;
    @FXML
    private static RadioButton normal;
    @FXML
    private static RadioButton players2;
    @FXML
    private static RadioButton players3;
    @FXML
    private static RadioButton players4;


    private GameCreation() {
    }

    /**
     * Initializes the scene.
     *
     * @param client The client to which change the state.
     * @throws IOException Thrown if there is an error somewhere.
     */
    public static void initialize(ClientGui client) throws IOException {
        GameCreation.client = client;
        scene = new Scene(FXMLLoader.load(Objects.requireNonNull(GameCreation.class.getResource("/fxml/game_creation.fxml"))));
        lookup();
        addEvents();
    }

    /**
     * Returns the scene.
     *
     * @return The scene.
     */
    public static Scene getScene() {
        create.requestFocus();
        players2.setSelected(true);
        normal.setSelected(true);
        return scene;
    }

    /**
     * Looks for every used element in the scene.
     */
    private static void lookup() {
        back = (Button) scene.lookup("#back");
        create = (Button) scene.lookup("#submit");
        expert = (RadioButton) scene.lookup("#expert");
        normal = (RadioButton) scene.lookup("#normal");
        players2 = (RadioButton) scene.lookup("#players_2");
        players3 = (RadioButton) scene.lookup("#players_3");
        players4 = (RadioButton) scene.lookup("#players_4");
    }

    /**
     * Adds all the events to the scene.
     */
    private static void addEvents() {

        AtomicInteger players = new AtomicInteger(2);
        AtomicBoolean expertMode = new AtomicBoolean(false);

        back.setOnMouseClicked(event -> {
            event.consume();
            if(!client.getController().getClientState().equals(ClientStates.CONNECTION_LOST))
                client.getController().setClientState(ClientStates.MAIN_MENU);
            client.changeScene();
        });

        create.setOnMouseClicked(event -> {
            event.consume();
            processButton(players.get(), expertMode.get());
        });

        create.setOnKeyPressed(event -> {
            event.consume();
            if (event.getCode() == KeyCode.ENTER)
                processButton(players.get(), expertMode.get());
        });

        expert.setOnMouseClicked(event -> {
            event.consume();
            expertMode.set(true);
            expert.setSelected(true);
            normal.setSelected(false);
        });

        normal.setOnMouseClicked(event -> {
            event.consume();
            expertMode.set(false);
            expert.setSelected(false);
            normal.setSelected(true);
        });

        players2.setOnMouseClicked(event -> {
            event.consume();
            players.set(2);
            players2.setSelected(true);
            players3.setSelected(false);
            players4.setSelected(false);
        });

        players3.setOnMouseClicked(event -> {
            event.consume();
            players.set(3);
            players2.setSelected(false);
            players3.setSelected(true);
            players4.setSelected(false);
        });

        players4.setOnMouseClicked(event -> {
            event.consume();
            players.set(4);
            players2.setSelected(false);
            players3.setSelected(false);
            players4.setSelected(true);
        });
    }

    private static void processButton(int player, boolean expertMode) {
        if(!client.getController().getClientState().equals(ClientStates.CONNECTION_LOST))
            client.getController().manageGameCreation(MessageCreator.gameCreation(player, expertMode));
        client.changeScene();
    }
}
