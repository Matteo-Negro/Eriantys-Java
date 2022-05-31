package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.view.ClientGui;
import it.polimi.ingsw.utilities.ClientStates;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class JoinGame {

    private static Scene scene = null;
    private static ClientGui client = null;

    @FXML
    private static Button back;
    @FXML
    private static List<TextField> code;
    @FXML
    private static Button enter;
    @FXML
    private static Label error;

    private JoinGame() {
    }

    /**
     * Initializes the scene.
     *
     * @param client The client to which change the state.
     * @throws IOException Thrown if there is an error somewhere.
     */
    public static void initialize(ClientGui client) throws IOException {
        JoinGame.client = client;
        scene = new Scene(FXMLLoader.load(Objects.requireNonNull(JoinGame.class.getResource("/fxml/join.fxml"))));
        lookup();
        addEvents();
    }

    /**
     * Returns the scene.
     *
     * @return The scene.
     */
    public static Scene getScene() {
        code.get(0).requestFocus();
        for (TextField field : code)
            field.setText("");
        return scene;
    }

    /**
     * Looks for every used element in the scene.
     */
    private static void lookup() {
        back = (Button) scene.lookup("#back");
        code = List.of(
                (TextField) scene.lookup("#code_1"),
                (TextField) scene.lookup("#code_2"),
                (TextField) scene.lookup("#code_3"),
                (TextField) scene.lookup("#code_4"),
                (TextField) scene.lookup("#code_5")
        );
        enter = (Button) scene.lookup("#submit");
        error = (Label) scene.lookup("#error");
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

        for (int index = 0; index < code.size(); index++)
            processCodeField(index);

        enter.setOnMouseClicked(event -> {
            event.consume();
            processButton(getCode());
        });

        enter.setOnKeyPressed(event -> {
            event.consume();
            if (event.getCode() == KeyCode.ENTER)
                processButton(getCode());
            else if (event.getCode() == KeyCode.BACK_SPACE) {
                code.get(code.size() - 1).requestFocus();
                code.get(code.size() - 1).deselect();
            }
        });

        scene.setOnMouseClicked(event -> {
            event.consume();
            processButton(getCode());
        });
    }

    private static void processCodeField(int index) {
        code.get(index).setEditable(false);
        code.get(index).setOnKeyPressed(event -> {
            event.consume();
            if (event.getCode() == KeyCode.ENTER)
                processButton(getCode());
            else if (event.getCode() == KeyCode.BACK_SPACE) {
                code.get(index).setText("");
                if (index != 0) {
                    code.get(index - 1).requestFocus();
                    code.get(index - 1).deselect();
                }
            } else if (event.getCode().isLetterKey() || event.getCode().isDigitKey()) {
                code.get(index).setText(event.getText().toUpperCase(Locale.ROOT));
                if (index != code.size() - 1) {
                    code.get(index + 1).requestFocus();
                    code.get(index + 1).deselect();
                } else
                    enter.requestFocus();
            }
        });
    }

    private static String getCode() {
        StringBuilder code = new StringBuilder();
        for (TextField field : JoinGame.code)
            code.append(field.getText());
        if (code.length() < JoinGame.code.size())
            return "";
        return code.toString();
    }

    private static void processButton(String code) {
        error.setText("Not implemented.");
        // TODO: enter game
        // client.changeScene(ClientStates.GAME_LOGIN);
    }
}
