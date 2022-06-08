package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.view.ClientGui;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.util.Objects;

public class Menu {

    private static Scene scene = null;
    private static ClientGui client = null;
    private static final Object lock = new Object();

    @FXML
    private static Button create;
    @FXML
    private static Button enter;
    @FXML
    private static Button exit;

    private Menu() {
    }

    /**
     * Initializes the scene.
     *
     * @param client The client to which change the state.
     * @throws IOException Thrown if there is an error somewhere.
     */
    public static void initialize(ClientGui client) throws IOException {
        Menu.client = client;
        scene = new Scene(FXMLLoader.load(Objects.requireNonNull(Menu.class.getResource("/fxml/menu.fxml"))));
        lookup();
        addEvents();
    }

    /**
     * Returns the scene.
     *
     * @return The scene.
     */
    public static Scene getScene() {
        synchronized (lock) {
            while (create == null) {
                try {
                    lock.wait(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException();
                }
            }
        }
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
            client.getController().manageMainMenu("1");
            client.changeScene();
        });

        create.setOnKeyPressed(event -> {
            event.consume();
            if (event.getCode() == KeyCode.ENTER)
                client.getController().manageMainMenu("1");
            client.changeScene();
        });

        enter.setOnMouseClicked(event -> {
            event.consume();
            client.getController().manageMainMenu("2");
            client.changeScene();
        });

        enter.setOnKeyPressed(event -> {
            event.consume();
            if (event.getCode() == KeyCode.ENTER)
                client.getController().manageMainMenu("2");
            client.changeScene();

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
