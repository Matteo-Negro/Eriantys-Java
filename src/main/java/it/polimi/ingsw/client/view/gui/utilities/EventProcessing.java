package it.polimi.ingsw.client.view.gui.utilities;

import it.polimi.ingsw.client.view.ClientGui;
import it.polimi.ingsw.utilities.ClientState;
import javafx.event.Event;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Static class for managing some common events from the GUI.
 */
public class EventProcessing {

    private EventProcessing() {
    }

    /**
     * Returns to the menu.
     *
     * @param event  The event that triggered the function.
     * @param client The ClientGui on which the operations have to be done.
     */
    public static void exit(Event event, ClientGui client) {
        if (!standard(event))
            return;
        if (!client.getController().getClientState().equals(ClientState.CONNECTION_LOST)) {
            if (client.getController().getClientState().equals(ClientState.GAME_RUNNING))
                client.getController().manageGameRunning("logout");
            else {
                client.getController().setClientState(ClientState.MAIN_MENU);
                client.changeScene();
            }
        }
    }

    /**
     * Toggles the board pane visibility.
     *
     * @param event The event that triggered the function.
     * @return true if it's the desired event, false otherwise.
     */
    public static boolean boardsToggle(Event event) {
        event.consume();
        return event instanceof KeyEvent keyEvent && keyEvent.getCode().equals(KeyCode.B);
    }

    /**
     * Toggles the special character pane visibility.
     *
     * @param event The event that triggered the function.
     * @return true if it's the desired event, false otherwise.
     */
    public static boolean specialCharactersToggle(Event event) {
        event.consume();
        return event instanceof KeyEvent keyEvent && keyEvent.getCode().equals(KeyCode.S);
    }

    /**
     * Standard event management.
     *
     * @param event The event that triggered the function.
     * @return true if it's the desired event, false otherwise.
     */
    public static boolean standard(Event event) {
        event.consume();
        return !(event instanceof KeyEvent keyEvent) || keyEvent.getCode().equals(KeyCode.ENTER);
    }

    /**
     * Manages the TextField submission.
     *
     * @param event The event that triggered the function.
     * @return true if it's the desired event, false otherwise.
     */
    public static boolean text(Event event) {
        event.consume();
        return event instanceof KeyEvent keyEvent && keyEvent.getCode().equals(KeyCode.ENTER);
    }
}
