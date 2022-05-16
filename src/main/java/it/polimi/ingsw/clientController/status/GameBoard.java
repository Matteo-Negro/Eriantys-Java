package it.polimi.ingsw.clientController.status;

import it.polimi.ingsw.utilities.HouseColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameBoard {
    private Map<HouseColor, Player> professors;
    private Island motherNatureIsland;
    private Player influenceBonus;
    private HouseColor ignoreColor;
    private List<Island> islands;
    private List<Cloud> clouds;
    private Map<Assistant, Player> playedAssistants;

    public GameBoard(Map<HouseColor, Player> statusProfessors, Island statusMotherNatureIsland, Player statusInfluenceBonus, HouseColor statusIgnoreColor, List<Island> statusIslands, List<Cloud> statusClouds, Map<Assistant, Player> statusPlayedAssistants){
        this.professors = new HashMap<>(statusProfessors);
        this.motherNatureIsland = statusMotherNatureIsland;
        this.influenceBonus = statusInfluenceBonus;
        this.ignoreColor = statusIgnoreColor;
        this.islands = new ArrayList<>(statusIslands);
        this.clouds = new ArrayList<>(statusClouds);
        this.playedAssistants = new HashMap<>(statusPlayedAssistants);
    }
}
