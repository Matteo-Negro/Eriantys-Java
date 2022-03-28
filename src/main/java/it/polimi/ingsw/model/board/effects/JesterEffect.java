package it.polimi.ingsw.model.board.effects;

import it.polimi.ingsw.model.utilities.HouseColor;

import java.util.HashMap;
import java.util.Map;

public class JesterEffect implements Effect {

    private Map<HouseColor, Integer> students;


    public JesterEffect() {
        students = new HashMap<>();
    }

    @Override
    public int getId() {
        return 7;
    }

    @Override
    public void effect() {

    }

    public void effect(Map<HouseColor, Integer> exchangedStudents) {
        exchangeStudents(exchangedStudents);
    }

    @Override
    public void clean() {
        students = new HashMap<>();
    }

    @Override
    public int getCost() {
        return 1;
    }

    private Map<HouseColor, Integer> getStudents() {
        return students;
    }

    private void exchangeStudents(Map<HouseColor, Integer> exchangedStudents) {
        students = exchangedStudents;
    }
}
