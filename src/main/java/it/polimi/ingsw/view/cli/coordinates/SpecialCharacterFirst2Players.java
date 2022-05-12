package it.polimi.ingsw.view.cli.coordinates;

import it.polimi.ingsw.view.cli.DeltaCoordinates;

/**
 * SpecialCharacter: first position in case of 2 player.
 */
public class SpecialCharacterFirst2Players implements DeltaCoordinates {

    private static SpecialCharacterFirst2Players instance = null;

    private SpecialCharacterFirst2Players() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static SpecialCharacterFirst2Players getInstance() {
        if (instance == null) instance = new SpecialCharacterFirst2Players();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return 13;
    }

    /**
     * Gets y coordinate.
     *
     * @return Y coordinate.
     */
    @Override
    public int getY() {
        return 15;
    }
}
