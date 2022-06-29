package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * SpecialCharacter: first position in case of 3 player.
 */
public class SpecialCharacterFirst3Players implements DeltaCoordinates {

    private static SpecialCharacterFirst3Players instance = null;

    private SpecialCharacterFirst3Players() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static SpecialCharacterFirst3Players getInstance() {
        if (instance == null) instance = new SpecialCharacterFirst3Players();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return 27;
    }

    /**
     * Gets y coordinate.
     *
     * @return Y coordinate.
     */
    @Override
    public int getY() {
        return 16;
    }
}
