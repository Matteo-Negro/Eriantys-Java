package it.polimi.ingsw.view.cli.coordinates;

import it.polimi.ingsw.view.cli.DeltaCoordinates;

/**
 * Island: cursor reset.
 */
public class IslandReset implements DeltaCoordinates {

    private static IslandReset instance = null;

    private IslandReset() {
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return -20;
    }

    /**
     * Gets y coordinate.
     *
     * @return Y coordinate.
     */
    @Override
    public int getY() {
        return -10;
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static IslandReset getInstance() {
        if (instance == null)
            instance = new IslandReset();
        return instance;
    }
}
