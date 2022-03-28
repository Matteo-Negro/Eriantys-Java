package it.polimi.ingsw.model.board.effects;

import it.polimi.ingsw.model.utilities.HouseColor;

import java.util.HashMap;
import java.util.Map;

public class PrincessEffect implements Effect {

    private final Map<HouseColor, Integer> students;

    public PrincessEffect() {
        students = new HashMap<>();
    }

    @Override
    public int getId() {
        return 11;
    }

    @Override
    public void effect() {

    }

    public void effect(HouseColor color) {
        takeStudent(color);
    }

    @Override
    public void clean() {

    }

    @Override
    public int getCost() {
        return 2;
    }

    private Map<HouseColor, Integer> getStudents() {
        return students;
    }

    private void takeStudent(HouseColor color) {

    }

    private void addStudent(HouseColor color) {

    }
}
