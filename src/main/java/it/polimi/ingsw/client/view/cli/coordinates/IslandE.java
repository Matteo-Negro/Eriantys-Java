package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * Island: E movement.
 */
public class IslandE implements DeltaCoordinates {

    private static IslandE instance = null;

    private IslandE() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static IslandE getInstance() {
        if (instance == null)
            instance = new IslandE();
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
