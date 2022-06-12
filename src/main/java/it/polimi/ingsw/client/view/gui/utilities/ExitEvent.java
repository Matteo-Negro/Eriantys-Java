package it.polimi.ingsw.client.view.gui.utilities;

import it.polimi.ingsw.client.view.ClientGui;
import it.polimi.ingsw.utilities.ClientState;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;

public class ExitEvent {

    // TODO: delete

    private ExitEvent() {
    }

    public static void addEvent(Button button, ClientGui client) {
        button.setOnMouseClicked(event -> {
            event.consume();
            if (!client.getController().getClientState().equals(ClientState.CONNECTION_LOST))
                client.getController().setClientState(ClientState.MAIN_MENU);
            client.changeScene();
        });

        button.setOnKeyPressed(event -> {
            event.consume();
            if (event.getCode() == KeyCode.ENTER &&
                    !client.getController().getClientState().equals(ClientState.CONNECTION_LOST))
                client.getController().setClientState(ClientState.MAIN_MENU);
            client.changeScene();
        });
    }
}
