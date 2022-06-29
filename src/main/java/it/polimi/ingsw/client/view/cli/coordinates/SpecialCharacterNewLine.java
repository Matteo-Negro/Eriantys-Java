package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * SpecialCharacter: new line movement.
 */
public class SpecialCharacterNewLine implements DeltaCoordinates {

    private static SpecialCharacterNewLine instance = null;

    private SpecialCharacterNewLine() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static SpecialCharacterNewLine getInstance() {
        if (instance == null)
            instance = new SpecialCharacterNewLine();
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
        return 1;
    }
}
