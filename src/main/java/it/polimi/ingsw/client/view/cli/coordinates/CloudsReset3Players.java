package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * Cloud: reset in case of 3 players.
 */
public class CloudsReset3Players implements DeltaCoordinates {

    private static CloudsReset3Players instance = null;

    private CloudsReset3Players() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static CloudsReset3Players getInstance() {
        if (instance == null)
            instance = new CloudsReset3Players();
        return instance;
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
        return -13;
    }
}
