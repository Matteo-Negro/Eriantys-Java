package it.polimi.ingsw.server.model.board.effects;

/**
 * Specific effect n.6.
 *
 * @author Riccardo Milici
 */
public class CentaurEffect implements Effect {

    /**
     * Returns the identification number of the specific effect object.
     *
     * @return id attribute.
     */
    @Override
    public int getId() {
        return 6;
    }

    /**
     * Returns the activation cost of the effect.
     *
     * @return cost attribute.
     */
    @Override
    public int getCost() {
        return 3;
    }
}
