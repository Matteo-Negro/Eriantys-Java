package it.polimi.ingsw.view.cli.coordinates;

import it.polimi.ingsw.view.cli.DeltaCoordinates;

/**
 * SpecialCharacter: cursor reset.
 */
public class SpecialCharacterStudentsReset implements DeltaCoordinates {

    private static SpecialCharacterStudentsReset instance = null;

    private SpecialCharacterStudentsReset() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static SpecialCharacterStudentsReset getInstance() {
        if (instance == null) instance = new SpecialCharacterStudentsReset();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return -9;
    }

    /**
     * Gets y coordinate.
     *
     * @return Y coordinate.
     */
    @Override
    public int getY() {
        return -9;
    }
}
