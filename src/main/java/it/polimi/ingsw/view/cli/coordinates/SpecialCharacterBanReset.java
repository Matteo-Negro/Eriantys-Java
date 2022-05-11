package it.polimi.ingsw.view.cli.coordinates;

import it.polimi.ingsw.view.cli.DeltaCoordinates;

/**
 * SpecialCharacter: cursor reset.
 */
public class SpecialCharacterBanReset implements DeltaCoordinates {

    private static SpecialCharacterBanReset instance = null;

    private SpecialCharacterBanReset() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static SpecialCharacterBanReset getInstance() {
        if (instance == null) instance = new SpecialCharacterBanReset();
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
        return -8;
    }
}
