package it.polimi.ingsw.model.board.effects;

import java.util.ArrayList;

public class CentaurEffect implements Effect {

    private List<Island> takenTowers;


    public CentaurEffect() {
        takenTowers = new ArrayList<Island>();
    }

    public int getId() {
        return 6;
    }

    public void effect(List<Island> towers) {
        takeTowers(towers);
    }

    public void clean() {
        returnTowers();
    }

    public int getCost() {
        return 3;
    }

    private List<Island> getTakenTowers() {
        return takenTowers;
    }

    private void takeTowers(List<Island> towers) {
        takenTowers = towers;
    }

    private void returnTowers() {
        takenTowers = new ArrayList<Island>();
    }
}
