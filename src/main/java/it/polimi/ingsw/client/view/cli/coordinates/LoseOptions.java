package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * Lose options: cursor position.
 */
public class LoseOptions implements DeltaCoordinates {

    private static LoseOptions instance = null;

    private LoseOptions() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static LoseOptions getInstance() {
        if (instance == null)
            instance = new LoseOptions();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return 17;
    }

    /**
     * Gets y coordinate.
     *
     * @return Y coordinate.
     */
    @Override
    public int getY() {
        return 10;
    }
}
