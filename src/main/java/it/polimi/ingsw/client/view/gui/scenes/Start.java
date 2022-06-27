package it.polimi.ingsw.client.view.gui.scenes;

import it.polimi.ingsw.client.view.ClientGui;
import it.polimi.ingsw.client.view.gui.utilities.EventProcessing;
import it.polimi.ingsw.utilities.ClientState;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.Socket;

/**
 * GUI scene for showing the splash screen.
 */
public class Start implements Prepare {

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
        ClientGui.link(ClientState.START_SCREEN, this);
    }

    /**
     * Prepares the scene for displaying.
     */
    @Override
    public void prepare() {
        Platform.runLater(() -> {
            connect.requestFocus();
            ip.setText("");
            port.setText("");
        });
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
