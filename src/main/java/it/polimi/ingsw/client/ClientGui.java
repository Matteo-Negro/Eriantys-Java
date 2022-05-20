package it.polimi.ingsw.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

public class ClientGui extends Application {


    /**
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages.
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene rootScene;
        try {
            rootScene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/SplashScreen.fxml"))));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        primaryStage.setScene(rootScene);
        primaryStage.setTitle("Eriantys");
        primaryStage.sizeToScene();
        primaryStage.setResizable(false);

        TextField ip = (TextField) rootScene.lookup("#ip");
        TextField port = (TextField) rootScene.lookup("#port");
        Label error = (Label) rootScene.lookup("#error");
        Button submit = (Button) rootScene.lookup("#submit");

        submit.requestFocus();

        rootScene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            processButton(ip, port, error);
            event.consume();
        });

        submit.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            processButton(ip, port, error);
            event.consume();
        });

        primaryStage.show();
    }

    private void processButton(TextField ip, TextField port, Label error) {
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
            Socket hostSocket = new Socket(socketIp, socketPort);
            hostSocket.setSoTimeout(10000);
        } catch (IOException e) {
            error.setText("Wrong data provided or server unreachable.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
