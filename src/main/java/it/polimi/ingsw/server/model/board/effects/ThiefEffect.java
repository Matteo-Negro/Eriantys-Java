package it.polimi.ingsw.server.model.board.effects;

/**
 * Specific effect n.12
 *
 * @author Riccardo Milici
 */

public class ThiefEffect implements Effect {

    @Override
    public int getId() {
        return 12;
    }

    @Override
    public int getCost() {
        return 3;
    }
}
