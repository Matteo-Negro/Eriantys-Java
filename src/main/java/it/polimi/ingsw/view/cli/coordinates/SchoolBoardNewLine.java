package it.polimi.ingsw.view.cli.coordinates;

import it.polimi.ingsw.view.cli.DeltaCoordinates;

/**
 * SchoolBoard: new line movement.
 */
public class SchoolBoardNewLine implements DeltaCoordinates {

    private static SchoolBoardNewLine instance = null;

    private SchoolBoardNewLine() {
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
        return 1;
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static SchoolBoardNewLine getInstance() {
        if (instance == null)
            instance = new SchoolBoardNewLine();
        return instance;
    }
}
