package it.polimi.ingsw.client.view.gui.utilities;

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
}
