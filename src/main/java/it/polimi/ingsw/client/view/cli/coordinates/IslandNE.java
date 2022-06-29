package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * Island: NE movement.
 */
public class IslandNE implements DeltaCoordinates {

    private static IslandNE instance = null;

    private IslandNE() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static IslandNE getInstance() {
        if (instance == null)
            instance = new IslandNE();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return 19;
    }

    /**
     * Gets y coordinate.
     *
     * @return Y coordinate.
     */
    @Override
    public int getY() {
        return -6;
    }
}
