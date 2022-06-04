package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.view.ClientGui;
import it.polimi.ingsw.utilities.ClientStates;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class Login {

    private static Scene scene = null;
    private static ClientGui client = null;

    @FXML
    private static Button back;
    @FXML
    private static Button login;
    @FXML
    private static TextField name;
    @FXML
    private static VBox names;

    private Login() {
    }

    /**
     * Initializes the scene.
     *
     * @param client The client to which change the state.
     * @throws IOException Thrown if there is an error somewhere.
     */
    public static void initialize(ClientGui client) throws IOException {
        Login.client = client;
        scene = new Scene(FXMLLoader.load(Objects.requireNonNull(Login.class.getResource("/fxml/login.fxml"))));
        lookup();
        addEvents();
    }

    /**
     * Returns the scene.
     *
     * @return The scene.
     */
    public static Scene getScene() {
        name.setText("");
        names.getChildren().clear();
        login.requestFocus();
        Platform.runLater(() -> {
            int index = client.getController().getGameModel().getWaitingRoom().size();
            for (Map.Entry<String, Boolean> entry : client.getController().getGameModel().getWaitingRoom().entrySet()) {
                Label label = new Label(entry.getKey());
                if (index != 1)
                    label.setOpaqueInsets(new Insets(0, 0, 10, 0));
                if (Boolean.TRUE.equals(entry.getValue()))
                    label.setTextFill(Color.rgb(181, 255, 181));
                else
                    label.setTextFill(Color.rgb(255, 181, 181));
                index--;
                names.getChildren().add(label);
            }
        });
        return scene;
    }

    /**
     * Looks for every used element in the scene.
     */
    private static void lookup() {
        back = (Button) scene.lookup("#back");
        login = (Button) scene.lookup("#submit");
        name = (TextField) scene.lookup("#name");
        names = (VBox) scene.lookup("#names");
    }

    /**
     * Adds all the events to the scene.
     */
    private static void addEvents() {
        back.setOnMouseClicked(event -> {
            event.consume();
            client.changeScene(ClientStates.MAIN_MENU);
        });

        back.setOnKeyPressed(event -> {
            event.consume();
            if (event.getCode() == KeyCode.ENTER)
                client.changeScene(ClientStates.MAIN_MENU);
        });

        login.setOnMouseClicked(event -> {
            event.consume();
            manageLogin();
        });

        login.setOnKeyPressed(event -> {
            event.consume();
            if (event.getCode() == KeyCode.ENTER)
                manageLogin();
        });

        back.setOnKeyPressed(event -> {
            event.consume();
            if (event.getCode() == KeyCode.ENTER)
                manageLogin();
        });
    }

    /**
     * Manages the login to the game.
     */
    private static void manageLogin() {
        client.getController().manageGameLogin(name.getText());
        client.changeScene();
    }
}
