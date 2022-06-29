package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * SpecialCharacter: first position in case of 3 or 4 player.
 */
public class SpecialCharacterFirst4Players implements DeltaCoordinates {

    private static SpecialCharacterFirst4Players instance = null;

    private SpecialCharacterFirst4Players() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static SpecialCharacterFirst4Players getInstance() {
        if (instance == null) instance = new SpecialCharacterFirst4Players();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return 13;
    }

    /**
     * Gets y coordinate.
     *
     * @return Y coordinate.
     */
    @Override
    public int getY() {
        return 30;
    }
}
