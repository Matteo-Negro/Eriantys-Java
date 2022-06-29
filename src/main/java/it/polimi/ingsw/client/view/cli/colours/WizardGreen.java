package it.polimi.ingsw.client.view.cli.colours;

/**
 * Wizard green colour.
 */
public class WizardGreen implements Colour {

    private static WizardGreen instance = null;

    private WizardGreen() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static WizardGreen getInstance() {
        if (instance == null)
            instance = new WizardGreen();
        return instance;
    }

    /**
     * Gets R second.
     *
     * @return R second.
     */
    @Override
    public int getR() {
        return 41;
    }

    /**
     * Gets G second.
     *
     * @return G second.
     */
    @Override
    public int getG() {
        return 178;
    }

    /**
     * Gets B second.
     *
     * @return B second.
     */
    @Override
    public int getB() {
        return 139;
    }
}
