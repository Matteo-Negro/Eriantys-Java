package it.polimi.ingsw.server.model.board.effects;

/**
 * Specific effect n.3.
 *
 * @author Riccardo Milici
 */
public class HeraldEffect implements Effect {

    /**
     * Returns the identification number of the specific effect object.
     *
     * @return id attribute.
     */
    @Override
    public int getId() {
        return 3;
    }

    @Override
    public int getCost() {
        return 3;
    }
}
