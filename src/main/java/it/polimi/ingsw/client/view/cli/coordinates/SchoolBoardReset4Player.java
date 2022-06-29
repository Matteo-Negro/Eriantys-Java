package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * SchoolBoard: cursor reset in case of 4 players.
 */
public class SchoolBoardReset4Player implements DeltaCoordinates {

    private static SchoolBoardReset4Player instance = null;

    private SchoolBoardReset4Player() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static SchoolBoardReset4Player getInstance() {
        if (instance == null) instance = new SchoolBoardReset4Player();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return -29;
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
