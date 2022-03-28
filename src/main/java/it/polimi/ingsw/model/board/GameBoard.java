package it.polimi.ingsw.model.board;


import it.polimi.ingsw.model.player.Assistant;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.utilities.HouseColor;

import java.util.List;
import java.util.Map;

/**
 * GameBoard Class
 *
 * @author Matteo Negro
 */

public class GameBoard {
    private Island motherNatureIsland;
    private Player influenceBonus;
    private HouseColor ignoreColor;
    private List<Assistant> playedAssistants;
    private List<Island> islands;
    private List<Cloud> clouds;
    private Bag bag;
    private List<SpecialCharacter> characters;

    public GameBoard() {
    }

    public void moveMotherNature(Island targetIsland, Assistant playedAssistant) {
    }

    public void addPlayedAssistant(String playerName, Assistant playedAssistant) {
    }

    public void flushAssistantsList() {
    }

    public void initializeClouds() {
    }

    public Map<Player, Integer> getInfluence() {
        return null;
    }

    public void removeEffects() {
    }

    public List<Island> getIslands() {
        return null;
    }

    public List<Cloud> getClouds() {
        return null;
    }

    public List<SpecialCharacter> getCharacters() {
        return null;
    }

    public Assistant getAssistant(int idAssistant) {
        return null;
    }

    public void setInfluenceBonus(Player targetPlayer) {
    }

    public void setIgnoreColor(HouseColor ingnoredColor) {
    }
}
