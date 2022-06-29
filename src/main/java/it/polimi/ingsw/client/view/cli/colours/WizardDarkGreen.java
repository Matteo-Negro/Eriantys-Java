package it.polimi.ingsw.client.view.cli.colours;

/**
 * Wizard dark green colour.
 */
public class WizardDarkGreen implements Colour {

    private static WizardDarkGreen instance = null;

    private WizardDarkGreen() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static WizardDarkGreen getInstance() {
        if (instance == null)
            instance = new WizardDarkGreen();
        return instance;
    }

    /**
     * Gets R second.
     *
     * @return R second.
     */
    @Override
    public int getR() {
        return 20;
    }

    /**
     * Gets G second.
     *
     * @return G second.
     */
    @Override
    public int getG() {
        return 89;
    }

    /**
     * Gets B second.
     *
     * @return B second.
     */
    @Override
    public int getB() {
        return 69;
    }
}
