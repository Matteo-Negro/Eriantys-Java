package it.polimi.ingsw.client.view.cli.colours;

import it.polimi.ingsw.client.view.cli.Colour;

/**
 * Wizard dark yellow colour.
 */
public class WizardDarkYellow implements Colour {

    private static WizardDarkYellow instance = null;

    private WizardDarkYellow() {
    }

    /**
     * Gets the instance of the class instead of generating a new one every time.
     *
     * @return The generated instance.
     */
    public static WizardDarkYellow getInstance() {
        if (instance == null)
            instance = new WizardDarkYellow();
        return instance;
    }

    /**
     * Gets R value.
     *
     * @return R value.
     */
    @Override
    public int getR() {
        return 124;
    }

    /**
     * Gets G value.
     *
     * @return G value.
     */
    @Override
    public int getG() {
        return 97;
    }

    /**
     * Gets B value.
     *
     * @return B value.
     */
    @Override
    public int getB() {
        return 44;
    }
}
