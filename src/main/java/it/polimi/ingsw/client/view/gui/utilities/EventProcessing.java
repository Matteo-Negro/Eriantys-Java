package it.polimi.ingsw.client.view.gui.utilities;

import it.polimi.ingsw.client.view.ClientGui;
import it.polimi.ingsw.utilities.ClientStates;
import javafx.event.Event;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class EventProcessing {

    private EventProcessing() {
    }

    public static boolean standard(Event event) {
        event.consume();
        return !(event instanceof KeyEvent keyEvent) || keyEvent.getCode().equals(KeyCode.ENTER);
    }

    public static void exit(Event event, ClientGui client) {
        if (!standard(event))
            return;
        if (!client.getController().getClientState().equals(ClientStates.CONNECTION_LOST))
            client.getController().setClientState(ClientStates.MAIN_MENU);
        client.changeScene();
    }
}
