package it.polimi.ingsw.model.utilities.exceptions;

/**
 * Exception thrown when the required professor is absent.
 *
 * @author Riccardo Motta
 */
public class NoProfessorException extends Exception {
    public NoProfessorException() {
        super();
    }

    public NoProfessorException(String message) {
        super(message);
    }
}
