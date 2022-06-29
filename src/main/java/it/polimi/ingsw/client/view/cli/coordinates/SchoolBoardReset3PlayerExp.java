package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * SchoolBoard: cursor reset in case of 3 players exp mode.
 */
public class SchoolBoardReset3PlayerExp implements DeltaCoordinates {

    private static SchoolBoardReset3PlayerExp instance = null;

    private SchoolBoardReset3PlayerExp() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static SchoolBoardReset3PlayerExp getInstance() {
        if (instance == null) instance = new SchoolBoardReset3PlayerExp();
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
        return -15;
    }
}
