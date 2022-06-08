package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.view.ClientGui;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

public class Start {

    private static Scene scene = null;
    private static ClientGui client = null;

    @FXML
    private static TextField ip;
    @FXML
    private static TextField port;
    @FXML
    private static Button submit;

    private Start() {
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
        Start.client = client;
        scene = new Scene(FXMLLoader.load(Objects.requireNonNull(Start.class.getResource("/fxml/splash_screen.fxml"))));
        lookup();
        addEvents();
    }

    /**
     * Returns the scene.
     *
     * @return The scene.
     */
    public static Scene getScene() {
        submit.requestFocus();
        return scene;
    }

    /**
     * Looks for every used element in the scene.
     */
    private static void lookup() {
        ip = (TextField) scene.lookup("#ip");
        port = (TextField) scene.lookup("#port");
        submit = (Button) scene.lookup("#submit");
    }

    /**
     * Adds all the events to the scene.
     */
    private static void addEvents() {

        submit.setOnMouseClicked(event -> {
            event.consume();
            processButton();
        });

        submit.setOnKeyPressed(event -> {
            event.consume();
            if (event.getCode() == KeyCode.ENTER)
                processButton();
        });
    }

    /**
     * Connects to the server.
     */
    private static void processButton() {

        String socketIp = ip.getText();
        int socketPort;

        if (socketIp.isEmpty())
            socketIp = "localhost";

        try {
            socketPort = Integer.parseInt(port.getText());
        } catch (NumberFormatException e) {
            socketPort = 36803;
        }

        try {
            client.getController().manageStartScreen(new Socket(socketIp, socketPort));
        } catch (Exception e) {
            client.showError("Wrong data provided or server unreachable.");
        }

        client.changeScene();
    }
}
