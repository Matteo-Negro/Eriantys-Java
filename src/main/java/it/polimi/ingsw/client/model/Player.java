package it.polimi.ingsw.client.model;

import it.polimi.ingsw.utilities.WizardType;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String name;
    private final WizardType wizard;
    private final int coins;
    private final SchoolBoard schoolBoard;
    private final List<Assistant> hand;

    public Player(String name, WizardType wizard, int coins, SchoolBoard statusSchoolBoard, List<Assistant> statusHand) {
        this.name = name;
        this.wizard = wizard;
        this.coins = coins;
        this.schoolBoard = statusSchoolBoard;
        this.hand = new ArrayList<>(statusHand);
    }
}
