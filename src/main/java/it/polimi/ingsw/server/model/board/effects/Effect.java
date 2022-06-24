package it.polimi.ingsw.server.model.board.effects;

/**
 * Interface used by the specific effect classes.
 *
 * @author Riccardo Milici
 */
public interface Effect {

    /**
     * Returns the identification number of the specific effect object.
     *
     * @return id attribute.
     */
    int getId();

    /**
     * Returns the activation cost of the effect.
     *
     * @return cost attribute.
     */
    int getCost();
}
