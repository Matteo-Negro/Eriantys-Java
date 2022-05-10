package it.polimi.ingsw.view.cli.coordinates;

import it.polimi.ingsw.view.cli.DeltaCoordinates;

/**
 * Title: new line movement.
 */
public class TitleNewLine implements DeltaCoordinates {

    private static TitleNewLine instance = null;

    private TitleNewLine() {
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return -67;
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

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static TitleNewLine getInstance() {
        if (instance == null)
            instance = new TitleNewLine();
        return instance;
    }
}
