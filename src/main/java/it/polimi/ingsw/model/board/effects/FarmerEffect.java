package it.polimi.ingsw.model.board.effects;

import java.util.Map;

public class FarmerEffect implements Effect {

    private Map<HouseColor, Player> stolenProfessors;

    public FarmerEffect() {
        stolenProfessors = new Map<HouseColor, Player>();
    }

    public int getId() {
        return 2;
    }

    public void effect(Map<HouseColor, Player> stolen) {
        stealProfessors(stolen);
    }

    public void clean() {
        returnProfessors();
    }

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
        stolenProfessors = new Map<HouseColor, Player>();
    }
}
