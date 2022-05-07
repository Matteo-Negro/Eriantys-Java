package it.polimi.ingsw.view.cli.coordinates;

import it.polimi.ingsw.view.cli.DeltaCoordinates;

/**
 * Cloud: E movement.
 */
public class CloudE implements DeltaCoordinates {

    private static CloudE instance = null;

    private CloudE() {
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return 20;
    }

    /**
     * Gets y coordinate.
     *
     * @return Y coordinate.
     */
    @Override
    public int getY() {
        return 0;
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static CloudE getInstance() {
        if (instance == null)
            instance = new CloudE();
        return instance;
    }
}
