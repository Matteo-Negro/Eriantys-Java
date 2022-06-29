package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * Island: last position.
 */
public class IslandLast implements DeltaCoordinates {

    private static IslandLast instance = null;

    private IslandLast() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static IslandLast getInstance() {
        if (instance == null)
            instance = new IslandLast();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return -98;
    }

    /**
     * Gets y coordinate.
     *
     * @return Y coordinate.
     */
    @Override
    public int getY() {
        return 11;
    }
}
