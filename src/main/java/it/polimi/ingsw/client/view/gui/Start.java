package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.view.ClientGui;
import it.polimi.ingsw.client.view.gui.utilities.EventProcessing;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.Socket;

public class Start {

    private ClientGui client;

    @FXML
    private TextField ip;
    @FXML
    private TextField port;
    @FXML
    private Button connect;

    /**
     * Initializes the scene.
     */
    public void initialize() {
        client = ClientGui.getInstance();
        Platform.runLater(() -> connect.requestFocus());
    }

    /**
     * Logs into the game.
     *
     * @param event The event that triggered the function.
     */
    @FXML
    private void connect(Event event) {

        if (!EventProcessing.standard(event))
            return;

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
    }
}
