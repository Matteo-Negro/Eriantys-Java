package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.ClientGui;
import it.polimi.ingsw.utilities.ClientStates;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.Objects;

public class MainMenu {

    private static Scene scene = null;
    private static ClientGui client = null;

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
        scene = new Scene(FXMLLoader.load(Objects.requireNonNull(MainMenu.class.getResource("/fxml/Menu.fxml"))));
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

        Button create = (Button) scene.lookup("#create");
        Button enter = (Button) scene.lookup("#enter");
        Button exit = (Button) scene.lookup("#exit");

        create.requestFocus();

        create.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            event.consume();
            client.changeScene(ClientStates.START_SCREEN);
        });

        enter.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            event.consume();
            client.changeScene(ClientStates.START_SCREEN);
        });

        exit.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            event.consume();
            client.changeScene(ClientStates.START_SCREEN);
        });

    }
}
