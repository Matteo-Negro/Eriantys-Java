package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * Game creation: cursor position.
 */
public class GameCreationOptions implements DeltaCoordinates {

    private static GameCreationOptions instance = null;

    private GameCreationOptions() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static GameCreationOptions getInstance() {
        if (instance == null)
            instance = new GameCreationOptions();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return 36;
    }

    /**
     * Gets y coordinate.
     *
     * @return Y coordinate.
     */
    @Override
    public int getY() {
        return 12;
    }
}
