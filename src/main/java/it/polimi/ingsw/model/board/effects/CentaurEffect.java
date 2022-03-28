package it.polimi.ingsw.model.board.effects;

import it.polimi.ingsw.model.board.Island;

import java.util.ArrayList;
import java.util.List;

public class CentaurEffect implements Effect {

    private List<Island> takenTowers;


    public CentaurEffect() {
        takenTowers = new ArrayList<>();
    }

    @Override
    public int getId() {
        return 6;
    }

    @Override
    public void effect() {

    }

    public void effect(List<Island> towers) {
        takeTowers(towers);
    }

    @Override
    public void clean() {
        returnTowers();
    }

    @Override
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
        takenTowers = new ArrayList<>();
    }
}
