package it.polimi.ingsw.view.cli.coordinates;

import it.polimi.ingsw.view.cli.DeltaCoordinates;

/**
 * Cloud: reset in case of 4 players.
 */
public class CloudsReset4Players implements DeltaCoordinates {

    private static CloudsReset4Players instance = null;

    private CloudsReset4Players() {
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return -82;
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
    public static CloudsReset4Players getInstance() {
        if (instance == null)
            instance = new CloudsReset4Players();
        return instance;
    }
}
