package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * Island: SE movement.
 */
public class IslandSE implements DeltaCoordinates {

    private static IslandSE instance = null;

    private IslandSE() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static IslandSE getInstance() {
        if (instance == null)
            instance = new IslandSE();
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
        return 6;
    }
}
