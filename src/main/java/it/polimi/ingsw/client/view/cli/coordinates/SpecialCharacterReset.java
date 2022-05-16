package it.polimi.ingsw.client.view.cli.coordinates;

import it.polimi.ingsw.client.view.cli.DeltaCoordinates;

/**
 * SpecialCharacter: cursor reset.
 */
public class SpecialCharacterReset implements DeltaCoordinates {

    private static SpecialCharacterReset instance = null;

    private SpecialCharacterReset() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static SpecialCharacterReset getInstance() {
        if (instance == null) instance = new SpecialCharacterReset();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return 0;
    }

    /**
     * Gets y coordinate.
     *
     * @return Y coordinate.
     */
    @Override
    public int getY() {
        return -7;
    }
}
