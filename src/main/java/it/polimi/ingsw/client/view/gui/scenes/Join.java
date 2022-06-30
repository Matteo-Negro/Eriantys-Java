package it.polimi.ingsw.client.view.gui.scenes;

import it.polimi.ingsw.client.view.ClientGui;
import it.polimi.ingsw.client.view.gui.utilities.EventProcessing;
import it.polimi.ingsw.utilities.ClientState;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * GUI scene for joining the game.
 */
public class Join implements Prepare {

    private ClientGui client;
    private List<TextField> code;

    @FXML
    private TextField code0;
    @FXML
    private TextField code1;
    @FXML
    private TextField code2;
    @FXML
    private TextField code3;
    @FXML
    private TextField code4;
    @FXML
    private Button enter;

    /**
     * Initializes the scene.
     */
    public void initialize() {
        client = ClientGui.getInstance();
        ClientGui.link(ClientState.JOIN_GAME, this);
        code = new ArrayList<>();
        code.add(code0);
        code.add(code1);
        code.add(code2);
        code.add(code3);
        code.add(code4);
        for (int index = 0; index < code.size(); index++)
            addEvent(index);
    }

    /**
     * Prepares the scene for displaying.
     */
    @Override
    public void prepare() {
        Platform.runLater(() -> {
            code0.requestFocus();
            for (TextField textField : code) {
                textField.setEditable(false);
                textField.setText("");
            }
        });
    }

    /**
     * Goes back to main menu.
     *
     * @param event The event that triggered the function.
     */
    @FXML
    private void back(Event event) {
        EventProcessing.exit(event, client);
    }

    /**
     * Enters the game.
     *
     * @param event The event that triggered the function.
     */
    @FXML
    private void enter(Event event) {
        EventProcessing.standard(event);
        if (event instanceof KeyEvent keyEvent) {
            if (keyEvent.getCode().equals(KeyCode.ENTER))
                processButton();
            else if (keyEvent.getCode() == KeyCode.BACK_SPACE)
                Platform.runLater(() -> {
                    code.get(code.size() - 1).requestFocus();
                    code.get(code.size() - 1).deselect();
                });
        } else
            processButton();
    }

    /**
     * Adds the events to every TextField.
     *
     * @param index The index of the TextField to process.
     */
    private void addEvent(int index) {
        code.get(index).setOnKeyPressed(event -> {
            event.consume();
            if (event.getCode() == KeyCode.ENTER)
                processButton();
            else if (event.getCode() == KeyCode.BACK_SPACE)
                Platform.runLater(() -> {
                    code.get(index).setText("");
                    if (index != 0) {
                        code.get(index - 1).requestFocus();
                        code.get(index - 1).deselect();
                        code.get(index - 1).setText("");
                    }
                });
            else if (event.getCode().isLetterKey() || event.getCode().isDigitKey())
                Platform.runLater(() -> {
                    code.get(index).setText(event.getText().toUpperCase(Locale.ROOT));
                    if (index != code.size() - 1) {
                        code.get(index + 1).requestFocus();
                        code.get(index + 1).deselect();
                    } else
                        enter.requestFocus();
                });
        });
    }

    /**
     * Gets the game code from the fields.
     *
     * @return The game code.
     */
    private String getCode() {
        StringBuilder string = new StringBuilder();
        for (TextField field : code)
            string.append(field.getText());
        if (string.length() < code.size())
            return "";
        return string.toString();
    }

    /**
     * Processes the entrance in the game.
     */
    private void processButton() {
        if (!client.getController().getClientState().equals(ClientState.CONNECTION_LOST))
            client.getController().manageJoinGame(getCode());
    }
}
