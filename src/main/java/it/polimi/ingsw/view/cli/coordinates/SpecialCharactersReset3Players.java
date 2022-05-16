package it.polimi.ingsw.view.cli.coordinates;

import it.polimi.ingsw.view.cli.DeltaCoordinates;

/**
 * SpecialCharacters: cursor reset in case of 2 players.
 */
public class SpecialCharactersReset3Players implements DeltaCoordinates {

    private static SpecialCharactersReset3Players instance = null;

    private SpecialCharactersReset3Players() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static SpecialCharactersReset3Players getInstance() {
        if (instance == null) instance = new SpecialCharactersReset3Players();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return -167;
    }

    /**
     * Gets y coordinate.
     *
     * @return Y coordinate.
     */
    @Override
    public int getY() {
        return -18;
    }
}