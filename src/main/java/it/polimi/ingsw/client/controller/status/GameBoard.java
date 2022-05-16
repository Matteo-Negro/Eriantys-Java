package it.polimi.ingsw.client.controller.status;

import it.polimi.ingsw.utilities.HouseColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameBoard {
    private final Map<HouseColor, Player> professors;
    private final Island motherNatureIsland;
    private final Player influenceBonus;
    private final HouseColor ignoreColor;
    private final List<Island> islands;
    private final List<Cloud> clouds;
    private final Map<Assistant, Player> playedAssistants;

    public GameBoard(Map<HouseColor, Player> statusProfessors, Island statusMotherNatureIsland, Player statusInfluenceBonus, HouseColor statusIgnoreColor, List<Island> statusIslands, List<Cloud> statusClouds, Map<Assistant, Player> statusPlayedAssistants) {
        this.professors = new HashMap<>(statusProfessors);
        this.motherNatureIsland = statusMotherNatureIsland;
        this.influenceBonus = statusInfluenceBonus;
        this.ignoreColor = statusIgnoreColor;
        this.islands = new ArrayList<>(statusIslands);
        this.clouds = new ArrayList<>(statusClouds);
        this.playedAssistants = new HashMap<>(statusPlayedAssistants);
    }
}
