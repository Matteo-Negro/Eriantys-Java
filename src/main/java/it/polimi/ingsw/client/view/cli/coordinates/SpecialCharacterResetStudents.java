package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * SpecialCharacter: cursor reset.
 */
public class SpecialCharacterResetStudents implements DeltaCoordinates {

    private static SpecialCharacterResetStudents instance = null;

    private SpecialCharacterResetStudents() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static SpecialCharacterResetStudents getInstance() {
        if (instance == null) instance = new SpecialCharacterResetStudents();
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
        return -10;
    }
}
