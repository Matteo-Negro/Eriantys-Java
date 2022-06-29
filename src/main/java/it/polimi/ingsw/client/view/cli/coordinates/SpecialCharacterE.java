package it.polimi.ingsw.client.view.cli.coordinates;

/**
 * SpecialCharacter: E movement.
 */
public class SpecialCharacterE implements DeltaCoordinates {

    private static SpecialCharacterE instance = null;

    private SpecialCharacterE() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static SpecialCharacterE getInstance() {
        if (instance == null) instance = new SpecialCharacterE();
        return instance;
    }

    /**
     * Gets x coordinate.
     *
     * @return X coordinate.
     */
    @Override
    public int getX() {
        return 10;
    }

    /**
     * Gets y coordinate.
     *
     * @return Y coordinate.
     */
    @Override
    public int getY() {
        return 0;
    }
}
