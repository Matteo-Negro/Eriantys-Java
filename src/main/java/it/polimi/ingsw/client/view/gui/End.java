package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.view.ClientGui;
import it.polimi.ingsw.utilities.ClientStates;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.util.Objects;

public class End {

    private static Scene scene = null;
    private static ClientGui client = null;

    @FXML
    private static Button exit;
    @FXML
    private static ImageView result;

    private static Image draw;
    private static Image lose;
    private static Image win;

    private End() {
    }

    /**
     * Initializes the scene.
     *
     * @param client The client to which change the state.
     * @throws IOException Thrown if there is an error somewhere.
     */
    public static void initialize(ClientGui client) throws IOException {
        End.client = client;
        scene = new Scene(FXMLLoader.load(Objects.requireNonNull(End.class.getResource("/fxml/end.fxml"))));
        draw = new Image("/titles/draw.png");
        lose = new Image("/titles/lose.png");
        win = new Image("/titles/win.png");
        lookup();
        addEvents();
    }

    /**
     * Returns the scene.
     *
     * @return The scene.
     */
    public static Scene getScene() {
        exit.requestFocus();
        result.setImage(switch (client.getController().getEndState()) {
            case DRAW -> draw;
            case LOST -> lose;
            case WON -> win;
        });
        return scene;
    }

    /**
     * Looks for every used element in the scene.
     */
    private static void lookup() {
        exit = (Button) scene.lookup("#exit");
        result = (ImageView) scene.lookup("#result");
    }

    /**
     * Adds all the events to the scene.
     */
    private static void addEvents() {
        exit.setOnMouseClicked(event -> {
            event.consume();
            client.changeScene(ClientStates.MAIN_MENU);
        });

        exit.setOnKeyPressed(event -> {
            event.consume();
            if (event.getCode() == KeyCode.ENTER)
                client.changeScene(ClientStates.MAIN_MENU);
        });
    }
}
