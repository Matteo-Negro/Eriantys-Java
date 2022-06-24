package it.polimi.ingsw.utilities.exceptions;

/**
 * Exception thrown when the required student is absent.
 *
 * @author Riccardo Motta
 */
public class NoStudentException extends Exception {

    /**
     * Default exception constructor.
     */
    public NoStudentException() {
        super();
    }

    /**
     * Default exception constructor.
     *
     * @param message The message to display.
     */
    public NoStudentException(String message) {
        super(message);
    }
}
