package it.polimi.ingsw.model.board.effects;

import it.polimi.ingsw.utilities.HouseColor;

import java.util.EnumMap;

/**
 * Specific effect n.9
 *
 * @author Riccardo Milici
 */

public class MushroomerEffect extends Effect {

    /**
     * Class constructor.
     * It creates an instance of the class.
     */
    public MushroomerEffect() {
    }

    @Override
    public int getId() {
        return 9;
    }

    @Override
    public void effect() {

    }

    @Override
    public int getCost() {
        return 3;
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
