package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.ClientGui;
import it.polimi.ingsw.utilities.ClientStates;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.Objects;

public class StartScreen {

    private static Scene scene = null;
    private static ClientGui client;
    private static boolean eventsAdded = false;

    private StartScreen() {
    }

    public static void initialize(ClientGui client) throws IOException {
        StartScreen.client = client;
        scene = new Scene(FXMLLoader.load(Objects.requireNonNull(StartScreen.class.getResource("/fxml/SplashScreen.fxml"))));
    }

    public static Scene getScene() {
        return scene;
    }

    public static void addEvents() {

        if (eventsAdded)
            return;

        TextField ip = (TextField) scene.lookup("#ip");
        TextField port = (TextField) scene.lookup("#port");
        Label error = (Label) scene.lookup("#error");
        Button submit = (Button) scene.lookup("#submit");

        submit.requestFocus();

        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            event.consume();
            if (event.getCode() == KeyCode.ENTER)
                processButton(ip, port, error);
        });

        submit.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            event.consume();
            processButton(ip, port, error);
        });

        eventsAdded = true;
    }

    private static void processButton(TextField ip, TextField port, Label error) {
        String socketIp = ip.getText();
        int socketPort;

        if (socketIp.isEmpty())
            socketIp = "localhost";

        try {
            socketPort = Integer.parseInt(port.getText());
        } catch (NumberFormatException e) {
            socketPort = 36803;
        }

//        try {
//            Socket hostSocket = new Socket(socketIp, socketPort);
//            hostSocket.setSoTimeout(10000);
//        } catch (IOException e) {
//            error.setText("Wrong data provided or server unreachable.");
//        }

        client.changeScene(ClientStates.MAIN_MENU);
    }
}