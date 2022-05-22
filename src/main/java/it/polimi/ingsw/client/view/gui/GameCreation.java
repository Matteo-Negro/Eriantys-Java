package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.ClientGui;
import it.polimi.ingsw.utilities.ClientStates;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class GameCreation {

    private static Scene scene = null;
    private static ClientGui client = null;

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
        scene = new Scene(FXMLLoader.load(Objects.requireNonNull(GameCreation.class.getResource("/fxml/GameCreation.fxml"))));
        addEvents();
    }

    /**
     * Returns the scene.
     *
     * @return The scene.
     */
    public static Scene getScene() {
        return scene;
    }

    /**
     * Adds all the events to the scene.
     */
    public static void addEvents() {

        Button back = (Button) scene.lookup("#back");
        Button create = (Button) scene.lookup("#submit");
        RadioButton expert = (RadioButton) scene.lookup("#expert");
        RadioButton normal = (RadioButton) scene.lookup("#normal");
        RadioButton players2 = (RadioButton) scene.lookup("#players_2");
        RadioButton players3 = (RadioButton) scene.lookup("#players_3");
        RadioButton players4 = (RadioButton) scene.lookup("#players_4");

        AtomicInteger players = new AtomicInteger(2);
        AtomicBoolean expertMode = new AtomicBoolean(false);

        create.requestFocus();
        players2.setSelected(true);
        normal.setSelected(true);

        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            event.consume();
            if (event.getCode() == KeyCode.ENTER)
                processButton(players.get(), expertMode.get());
        });

        back.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            event.consume();
            client.changeScene(ClientStates.MAIN_MENU);
        });

        create.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            event.consume();
            processButton(players.get(), expertMode.get());
        });

        expert.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            event.consume();
            expertMode.set(true);
            expert.setSelected(true);
            normal.setSelected(false);
        });

        normal.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            event.consume();
            expertMode.set(false);
            expert.setSelected(false);
            normal.setSelected(true);
        });

        players2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            event.consume();
            players.set(2);
            players2.setSelected(true);
            players3.setSelected(false);
            players4.setSelected(false);
        });

        players3.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            event.consume();
            players.set(3);
            players2.setSelected(false);
            players3.setSelected(true);
            players4.setSelected(false);
        });

        players4.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            event.consume();
            players.set(4);
            players2.setSelected(false);
            players3.setSelected(false);
            players4.setSelected(true);
        });
    }

    private static void processButton(int player, boolean expertMode) {
        // TODO: create new game
        client.changeScene(ClientStates.START_SCREEN);
    }
}
