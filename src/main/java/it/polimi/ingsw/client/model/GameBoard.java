package it.polimi.ingsw.client.model;

import it.polimi.ingsw.utilities.HouseColor;

import java.util.*;

public class GameBoard {
    private Map<HouseColor, Player> professors;
    private Island motherNatureIsland;
    private Player influenceBonus;
    private HouseColor ignoreColor;
    private List<Island> islands;
    private List<Cloud> clouds;
    private List<SpecialCharacter> specialCharacters;

    public GameBoard(Map<HouseColor, Player> statusProfessors, Island statusMotherNatureIsland, Player statusInfluenceBonus, HouseColor statusIgnoreColor, List<Island> statusIslands, List<Cloud> statusClouds, List<SpecialCharacter> statusSpecialCharacters) {
        this.professors = new EnumMap<>(statusProfessors);
        this.motherNatureIsland = statusMotherNatureIsland;
        this.influenceBonus = statusInfluenceBonus;
        this.ignoreColor = statusIgnoreColor;
        this.islands = new ArrayList<>(statusIslands);
        this.clouds = new ArrayList<>(statusClouds);
        this.specialCharacters = new ArrayList<>(statusSpecialCharacters);
    }
}
