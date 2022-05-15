package it.polimi.ingsw.clientController;

import it.polimi.ingsw.utilities.WizardType;

public class Player {
    private String name;
    private WizardType wizard;
    private int coins;

    public Player(String name, WizardType wizard, int coins){
        this.name = name;
        this.wizard = wizard;
        this.coins = coins;
    }
}
