package it.polimi.ingsw.utilities.exceptions;

/**
 * Exception thrown when a player sends an exit command.
 *
 * @author Riccardo Milici.
 */
public class ExitException extends Exception {
    public ExitException() {
        super();
    }

    public ExitException(String message) {
        super(message);
    }
}