package it.polimi.ingsw.utilities.exceptions;

/**
 * Exception thrown when someone tries to take a ban from the card, but bans are finished.
 *
 * @author Riccardo Motta
 */
public class NoMoreBansLeftException extends Exception {

    /**
     * Default exception constructor.
     */
    public NoMoreBansLeftException() {
        super();
    }

    /**
     * Default exception constructor.
     *
     * @param message The message to display.
     */
    public NoMoreBansLeftException(String message) {
        super(message);
    }
}
