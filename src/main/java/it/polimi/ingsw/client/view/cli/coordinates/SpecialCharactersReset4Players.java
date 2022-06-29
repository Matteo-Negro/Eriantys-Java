package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * SpecialCharacters: cursor reset in case of 3 or 4 players.
 */
public class SpecialCharactersReset4Players implements DeltaCoordinates {

    private static SpecialCharactersReset4Players instance = null;

    private SpecialCharactersReset4Players() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static SpecialCharactersReset4Players getInstance() {
        if (instance == null) instance = new SpecialCharactersReset4Players();
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
        return -33;
    }
}
