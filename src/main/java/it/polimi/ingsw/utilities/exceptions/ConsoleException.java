package it.polimi.ingsw.utilities.exceptions;

/**
 * Exception thrown when there is an error processing the cli.
 *
 * @author Riccardo Motta
 */
public class ConsoleException extends Exception {
    public ConsoleException() {
        super();
    }

    public ConsoleException(String message) {
        super(message);
    }
}
