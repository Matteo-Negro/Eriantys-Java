package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * Game code: cursor position.
 */
public class GameCode implements DeltaCoordinates {

    private static GameCode instance = null;

    private GameCode() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static GameCode getInstance() {
        if (instance == null)
            instance = new GameCode();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return 28;
    }

    /**
     * Gets y coordinate.
     *
     * @return Y coordinate.
     */
    @Override
    public int getY() {
        return 15;
    }
}
