package it.polimi.ingsw.server.model.board.effects;

/**
 * Specific effect n.4
 *
 * @author Riccardo Milici
 */

public class MessengerEffect implements Effect {

    @Override
    public int getId() {
        return 4;
    }

    @Override
    public int getCost() {
        return 1;
    }
}
