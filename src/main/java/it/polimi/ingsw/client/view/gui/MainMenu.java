package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.view.ClientGui;
import it.polimi.ingsw.utilities.ClientStates;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.util.Objects;

public class MainMenu {

    private static Scene scene = null;
    private static ClientGui client = null;

    @FXML
    private static Button create;
    @FXML
    private static Button enter;
    @FXML
    private static Button exit;

    private MainMenu() {
    }

    /**
     * Initializes the scene.
     *
     * @param client The client to which change the state.
     * @throws IOException Thrown if there is an error somewhere.
     */
    public static void initialize(ClientGui client) throws IOException {
        MainMenu.client = client;
        scene = new Scene(FXMLLoader.load(Objects.requireNonNull(MainMenu.class.getResource("/fxml/menu.fxml"))));
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
        return scene;
    }

    /**
     * Looks for every used element in the scene.
     */
    private static void lookup() {
        create = (Button) scene.lookup("#create");
        enter = (Button) scene.lookup("#enter");
        exit = (Button) scene.lookup("#exit");
    }

    /**
     * Adds all the events to the scene.
     */
    private static void addEvents() {

        create.setOnMouseClicked(event -> {
            event.consume();
            client.changeScene(ClientStates.GAME_CREATION);
        });

        create.setOnKeyPressed(event -> {
            event.consume();
            if (event.getCode() == KeyCode.ENTER)
                client.changeScene(ClientStates.GAME_CREATION);
        });

        enter.setOnMouseClicked(event -> {
            event.consume();
            client.changeScene(ClientStates.JOIN_GAME);
        });

        enter.setOnKeyPressed(event -> {
            event.consume();
            if (event.getCode() == KeyCode.ENTER)
                client.changeScene(ClientStates.JOIN_GAME);
        });

        exit.setOnMouseClicked(event -> {
            event.consume();
            manageExit();
        });

        exit.setOnKeyPressed(event -> {
            event.consume();
            if (event.getCode() == KeyCode.ENTER)
                manageExit();
        });
    }

    /**
     * Logs of from the server.
     */
    private static void manageExit() {
        client.getController().manageConnectionLost();
        client.changeScene();
    }
}
