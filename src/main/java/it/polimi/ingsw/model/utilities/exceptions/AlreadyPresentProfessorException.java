package it.polimi.ingsw.model.utilities.exceptions;

/**
 * Exception thrown when the professor was already in that position.
 *
 * @author Riccardo Motta
 */
public class AlreadyPresentProfessorException extends Exception {
    public AlreadyPresentProfessorException() {
        super();
    }

    public AlreadyPresentProfessorException(String message) {
        super(message);
    }
}
