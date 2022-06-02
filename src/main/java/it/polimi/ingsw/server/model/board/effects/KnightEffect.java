package it.polimi.ingsw.server.model.board.effects;

/**
 * Specific effect n.8
 *
 * @author Riccardo Milici
 */

public class KnightEffect implements Effect {

    @Override
    public int getId() {
        return 8;
    }

    @Override
    public int getCost() {
        return 2;
    }
}
