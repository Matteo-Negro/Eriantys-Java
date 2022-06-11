package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.view.ClientGui;
import javafx.application.Platform;
import javafx.event.ActionEvent;
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

    public void initialize() {
        client = ClientGui.getInstance();
        Platform.runLater(() -> connect.requestFocus());
    }

    @FXML
    private void processButton(Event event) {

        event.consume();

        if (event instanceof KeyEvent keyEvent && keyEvent.getCode() != KeyCode.ENTER)
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
