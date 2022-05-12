package it.polimi.ingsw.view.cli.coordinates;

import it.polimi.ingsw.view.cli.DeltaCoordinates;

/**
 * Subtitle: cursor reset.
 */
public class SubtitleReset implements DeltaCoordinates {

    private static SubtitleReset instance = null;

    private SubtitleReset() {
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
        return -1;
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static SubtitleReset getInstance() {
        if (instance == null)
            instance = new SubtitleReset();
        return instance;
    }
}
