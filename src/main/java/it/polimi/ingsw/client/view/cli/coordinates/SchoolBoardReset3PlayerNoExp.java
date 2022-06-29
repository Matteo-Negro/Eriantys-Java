package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * SchoolBoard: cursor reset in case of 3 players, no exp.
 */
public class SchoolBoardReset3PlayerNoExp implements DeltaCoordinates {

    private static SchoolBoardReset3PlayerNoExp instance = null;

    private SchoolBoardReset3PlayerNoExp() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static SchoolBoardReset3PlayerNoExp getInstance() {
        if (instance == null) instance = new SchoolBoardReset3PlayerNoExp();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return -13;
    }

    /**
     * Gets y coordinate.
     *
     * @return Y coordinate.
     */
    @Override
    public int getY() {
        return -15;
    }
}
