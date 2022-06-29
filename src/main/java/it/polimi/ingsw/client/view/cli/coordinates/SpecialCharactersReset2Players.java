package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * SpecialCharacters: cursor reset in case of 2 players.
 */
public class SpecialCharactersReset2Players implements DeltaCoordinates {

    private static SpecialCharactersReset2Players instance = null;

    private SpecialCharactersReset2Players() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static SpecialCharactersReset2Players getInstance() {
        if (instance == null) instance = new SpecialCharactersReset2Players();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return -153;
    }

    /**
     * Gets y coordinate.
     *
     * @return Y coordinate.
     */
    @Override
    public int getY() {
        return -17;
    }
}
