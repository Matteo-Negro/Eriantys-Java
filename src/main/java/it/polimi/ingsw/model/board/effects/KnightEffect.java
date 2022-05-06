package it.polimi.ingsw.model.board.effects;

import it.polimi.ingsw.utilities.HouseColor;

import java.util.EnumMap;

/**
 * Specific effect n.8
 *
 * @author Riccardo Milici
 */

public class KnightEffect extends Effect {

    /**
     * Class constructor.
     * It creates an instance of the class.
     */
    public KnightEffect() {
    }

    @Override
    public int getId() {
        return 8;
    }

    @Override
    public void effect() {

    }

    @Override
    public int getCost() {
        return 2;
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
