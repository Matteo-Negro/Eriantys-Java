package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * Game creation: cursor position for first input.
 */
public class GameCreationFirstInput implements DeltaCoordinates {

    private static GameCreationFirstInput instance = null;

    private GameCreationFirstInput() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static GameCreationFirstInput getInstance() {
        if (instance == null)
            instance = new GameCreationFirstInput();
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
        return 1;
    }
}
