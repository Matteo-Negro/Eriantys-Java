package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * Cloud: E movement.
 */
public class CloudE implements DeltaCoordinates {

    private static CloudE instance = null;

    private CloudE() {
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
}
