package it.polimi.ingsw.client.view.cli.colours;

/**
 * Wizard fuchsia colour.
 */
public class WizardFuchsia implements Colour {

    private static WizardFuchsia instance = null;

    private WizardFuchsia() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static WizardFuchsia getInstance() {
        if (instance == null)
            instance = new WizardFuchsia();
        return instance;
    }

    /**
     * Gets R second.
     *
     * @return R second.
     */
    @Override
    public int getR() {
        return 184;
    }

    /**
     * Gets G second.
     *
     * @return G second.
     */
    @Override
    public int getG() {
        return 133;
    }

    /**
     * Gets B second.
     *
     * @return B second.
     */
    @Override
    public int getB() {
        return 170;
    }
}
