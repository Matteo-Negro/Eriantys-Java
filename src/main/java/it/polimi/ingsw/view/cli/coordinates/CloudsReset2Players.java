package it.polimi.ingsw.view.cli.coordinates;

import it.polimi.ingsw.view.cli.DeltaCoordinates;

/**
 * Cloud: reset in case of 2 players.
 */
public class CloudsReset2Players implements DeltaCoordinates {

    private static CloudsReset2Players instance = null;

    private CloudsReset2Players() {
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return -62;
    }

    /**
     * Gets y coordinate.
     *
     * @return Y coordinate.
     */
    @Override
    public int getY() {
        return -13;
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static CloudsReset2Players getInstance() {
        if (instance == null)
            instance = new CloudsReset2Players();
        return instance;
    }
}
