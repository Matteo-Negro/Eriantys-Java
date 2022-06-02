package it.polimi.ingsw.client.view.cli.coordinates;

import it.polimi.ingsw.client.view.cli.DeltaCoordinates;

/**
 * Win options: cursor position.
 */
public class WinOptions implements DeltaCoordinates {

    private static WinOptions instance = null;

    private WinOptions() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static WinOptions getInstance() {
        if (instance == null)
            instance = new WinOptions();
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
        return 10;
    }
}