package it.polimi.ingsw.server.model.board.effects;

/**
 * Interface used by the specific effect classes
 *
 * @author Riccardo Milici
 */

public abstract class Effect {

    /**
     * Returns the identification number of the specific effect object.
     *
     * @return id attribute
     */
    public abstract int getId();

    /**
     * Activates the effect of the object.
     */
    public abstract void effect();

    /**
     * returns the activation cost of the effect.
     *
     * @return cost attribute
     */
    public abstract int getCost();
}