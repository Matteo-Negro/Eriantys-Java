package it.polimi.ingsw.server.model.board.effects;

/**
 * Specific effect n.10
 *
 * @author Riccardo Milici
 */

public class MinstrelEffect implements Effect {

    @Override
    public int getId() {
        return 10;
    }

    @Override
    public int getCost() {
        return 1;
    }
}
