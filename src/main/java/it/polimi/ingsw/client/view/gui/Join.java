package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.view.ClientGui;
import it.polimi.ingsw.client.view.gui.utilities.ExitEvent;
import it.polimi.ingsw.utilities.ClientStates;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Join {

    private static Scene scene = null;
    private static ClientGui client = null;

    @FXML
    private static Button back;
    @FXML
    private static List<TextField> code;
    @FXML
    private static Button enter;

    private Join() {
    }

    /**
     * Initializes the scene.
     *
     * @param client The client to which change the state.
     * @throws IOException Thrown if there is an error somewhere.
     */
    public static void initialize(ClientGui client) throws IOException {
        Join.client = client;
        scene = new Scene(FXMLLoader.load(Objects.requireNonNull(Join.class.getResource("/fxml/join.fxml"))));
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
    }

    /**
     * Adds all the events to the scene.
     */
    private static void addEvents() {

        ExitEvent.addEvent(back, client);

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
        for (TextField field : Join.code)
            code.append(field.getText());
        if (code.length() < Join.code.size())
            return "";
        return code.toString();
    }

    private static void processButton(String code) {
        if (!client.getController().getClientState().equals(ClientStates.CONNECTION_LOST))
            client.getController().manageJoinGame(code);
        client.changeScene();
    }
}
