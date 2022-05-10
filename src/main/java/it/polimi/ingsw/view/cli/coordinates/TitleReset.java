package it.polimi.ingsw.view.cli.coordinates;

import it.polimi.ingsw.view.cli.DeltaCoordinates;

/**
 * Title: cursor reset.
 */
public class TitleReset implements DeltaCoordinates {

    private static TitleReset instance = null;

    private TitleReset() {
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return -67;
    }

    /**
     * Gets y coordinate.
     *
     * @return Y coordinate.
     */
    @Override
    public int getY() {
        return -12;
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static TitleReset getInstance() {
        if (instance == null)
            instance = new TitleReset();
        return instance;
    }
}
