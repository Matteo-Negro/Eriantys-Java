package it.polimi.ingsw.model.board.effects;

import it.polimi.ingsw.utilities.HouseColor;

import java.util.EnumMap;

/**
 * Specific effect n.4
 *
 * @author Riccardo Milici
 */

public class MessengerEffect extends Effect {

    /**
     * Class constructor.
     * It creates an instance of the class.
     */
    public MessengerEffect() {

    }

    @Override
    public int getId() {
        return 4;
    }

    @Override
    public void effect() {

    }

    @Override
    public int getCost() {
        return 1;
    }

    /**
     * Returns the map saved in the students attribute.
     *
     * @return students attribute.
     */
    public EnumMap<HouseColor, Integer> getStudents() {
        return null;
    }
}
