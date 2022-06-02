package it.polimi.ingsw.utilities.exceptions;

/**
 * Exception thrown when someone tries to take a ban from the card, but bans are finished.
 *
 * @author Riccardo Motta
 */
public class NoMoreBansLeftException extends Exception {
    public NoMoreBansLeftException() {
        super();
    }

    public NoMoreBansLeftException(String message) {
        super(message);
    }
}
