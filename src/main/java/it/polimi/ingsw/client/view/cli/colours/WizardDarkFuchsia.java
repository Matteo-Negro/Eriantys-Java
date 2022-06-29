package it.polimi.ingsw.client.view.cli.colours;

/**
 * Wizard dark fuchsia colour.
 */
public class WizardDarkFuchsia implements Colour {

    private static WizardDarkFuchsia instance = null;

    private WizardDarkFuchsia() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static WizardDarkFuchsia getInstance() {
        if (instance == null)
            instance = new WizardDarkFuchsia();
        return instance;
    }

    /**
     * Gets R second.
     *
     * @return R second.
     */
    @Override
    public int getR() {
        return 92;
    }

    /**
     * Gets G second.
     *
     * @return G second.
     */
    @Override
    public int getG() {
        return 67;
    }

    /**
     * Gets B second.
     *
     * @return B second.
     */
    @Override
    public int getB() {
        return 85;
    }
}
