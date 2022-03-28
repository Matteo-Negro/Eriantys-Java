package it.polimi.ingsw.model.board.effects;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.utilities.HouseColor;

import java.util.HashMap;
import java.util.Map;

public class FarmerEffect implements Effect {

    private Map<HouseColor, Player> stolenProfessors;

    public FarmerEffect() {
        stolenProfessors = new HashMap<>();
    }

    @Override
    public int getId() {
        return 2;
    }

    @Override
    public void effect() {

    }

    public void effect(Map<HouseColor, Player> stolen) {
        stealProfessors(stolen);
    }

    @Override
    public void clean() {
        returnProfessors();
    }

    @Override
    public int getCost() {
        return 2;
    }

    private Map<HouseColor, Player> getStolenProfessors() {
        return stolenProfessors;
    }

    private void stealProfessors(Map<HouseColor, Player> stolen) {
        stolenProfessors = stolen;
    }

    private void returnProfessors() {
        stolenProfessors = new HashMap<>();
    }
}
