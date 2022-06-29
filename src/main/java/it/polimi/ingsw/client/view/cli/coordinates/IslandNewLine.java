package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * Island: new line movement.
 */
public class IslandNewLine implements DeltaCoordinates {

    private static IslandNewLine instance = null;

    private IslandNewLine() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static IslandNewLine getInstance() {
        if (instance == null)
            instance = new IslandNewLine();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return -20;
    }

    /**
     * Gets y coordinate.
     *
     * @return Y coordinate.
     */
    @Override
    public int getY() {
        return 1;
    }
}
