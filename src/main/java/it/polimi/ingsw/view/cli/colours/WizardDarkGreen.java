package it.polimi.ingsw.view.cli.colours;

import it.polimi.ingsw.view.cli.Colour;

/**
 * Wizard dark green colour.
 */
public class WizardDarkGreen implements Colour {

    private static WizardDarkGreen instance = null;

    private WizardDarkGreen() {
    }

    /**
     * Gets R value.
     *
     * @return R value.
     */
    @Override
    public int getR() {
        return 15;
    }

    /**
     * Gets G value.
     *
     * @return G value.
     */
    @Override
    public int getG() {
        return 64;
    }

    /**
     * Gets B value.
     *
     * @return B value.
     */
    @Override
    public int getB() {
        return 50;
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
}
