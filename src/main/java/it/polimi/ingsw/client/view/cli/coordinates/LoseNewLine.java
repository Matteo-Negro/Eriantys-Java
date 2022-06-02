package it.polimi.ingsw.client.view.cli.coordinates;

import it.polimi.ingsw.client.view.cli.DeltaCoordinates;

/**
 * Lose page: new line movement.
 */
public class LoseNewLine implements DeltaCoordinates {

    private static LoseNewLine instance = null;

    private LoseNewLine() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static LoseNewLine getInstance() {
        if (instance == null)
            instance = new LoseNewLine();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return -40;
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