package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * Island: first position.
 */
public class IslandFirst implements DeltaCoordinates {

    private static IslandFirst instance = null;

    private IslandFirst() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static IslandFirst getInstance() {
        if (instance == null)
            instance = new IslandFirst();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return 0;
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
