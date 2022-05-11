package it.polimi.ingsw.view.cli.coordinates;

import it.polimi.ingsw.view.cli.DeltaCoordinates;

/**
 * Island: Third position (only in case of 3 players).
 */
public class SchoolBoardThird implements DeltaCoordinates {

    private static SchoolBoardThird instance = null;

    private SchoolBoardThird() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static SchoolBoardThird getInstance() {
        if (instance == null) instance = new SchoolBoardThird();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return 14;
    }

    /**
     * Gets y coordinate.
     *
     * @return Y coordinate.
     */
    @Override
    public int getY() {
        return 15;
    }
}
