package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * Game code: cursor position for input.
 */
public class GameCodeInput implements DeltaCoordinates {

    private static GameCodeInput instance = null;

    private GameCodeInput() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static GameCodeInput getInstance() {
        if (instance == null)
            instance = new GameCodeInput();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return 12;
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
