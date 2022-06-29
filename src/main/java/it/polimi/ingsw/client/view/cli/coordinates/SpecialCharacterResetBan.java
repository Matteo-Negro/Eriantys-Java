package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * SpecialCharacter: cursor reset.
 */
public class SpecialCharacterResetBan implements DeltaCoordinates {

    private static SpecialCharacterResetBan instance = null;

    private SpecialCharacterResetBan() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static SpecialCharacterResetBan getInstance() {
        if (instance == null) instance = new SpecialCharacterResetBan();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return -9;
    }

    /**
     * Gets y coordinate.
     *
     * @return Y coordinate.
     */
    @Override
    public int getY() {
        return -9;
    }
}
