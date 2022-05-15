package it.polimi.ingsw.clientController;

import it.polimi.ingsw.utilities.HouseColor;

import java.util.HashMap;
import java.util.Map;

public class GameBoard {
    private Map<HouseColor, Player> professors;
    private Island motherNatureIsland;
    private Player influenceBonus;
    private HouseColor ignoreColor;

    public GameBoard(Map<HouseColor, Player> statusProfessors, Island statusMotherNatureIsland, Player statusInfluenceBonus, HouseColor statusIgnoreColor){
        this.professors = new HashMap<>(statusProfessors);
        this.motherNatureIsland = statusMotherNatureIsland;
        this.influenceBonus = statusInfluenceBonus;
        this.ignoreColor = statusIgnoreColor;
    }
}
