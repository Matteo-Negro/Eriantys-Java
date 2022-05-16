package it.polimi.ingsw.clientController.status;

import com.sun.source.tree.Scope;
import it.polimi.ingsw.utilities.WizardType;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String name;
    private final WizardType wizard;
    private int coins;
    private SchoolBoard schoolBoard;
    private List<Assistant> hand;

    public Player(String name, WizardType wizard, int coins, SchoolBoard statusSchoolBoard, List<Assistant> statusHand){
        this.name = name;
        this.wizard = wizard;
        this.coins = coins;
        this.schoolBoard = statusSchoolBoard;
        this.hand = new ArrayList<>(statusHand);
    }
}
