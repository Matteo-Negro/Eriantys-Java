package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * Cloud: first position in case of 3 players.
 */
public class CloudFirst3Players implements DeltaCoordinates {

    private static CloudFirst3Players instance = null;

    private CloudFirst3Players() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static CloudFirst3Players getInstance() {
        if (instance == null)
            instance = new CloudFirst3Players();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return 32;
    }

    /**
     * Gets y coordinate.
     *
     * @return Y coordinate.
     */
    @Override
    public int getY() {
        return 13;
    }
}
