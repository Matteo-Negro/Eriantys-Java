package it.polimi.ingsw.view.cli.coordinates;

import it.polimi.ingsw.view.cli.DeltaCoordinates;

/**
 * SchoolBoard: cursor reset.
 */
public class SchoolBoardReset implements DeltaCoordinates {

    private static SchoolBoardReset instance = null;

    private SchoolBoardReset() {
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return -72;
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

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static SchoolBoardReset getInstance() {
        if (instance == null)
            instance = new SchoolBoardReset();
        return instance;
    }
}
