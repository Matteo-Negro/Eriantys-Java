package it.polimi.ingsw.utilities.exceptions;

/**
 * Exception thrown when the required student is absent.
 *
 * @author Riccardo Motta
 */
public class NoStudentException extends Exception {
    public NoStudentException() {
        super();
    }

    public NoStudentException(String message) {
        super(message);
    }
}
