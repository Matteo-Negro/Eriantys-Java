package it.polimi.ingsw.model.board.effects;

import it.polimi.ingsw.model.utilities.HouseColor;

import java.util.HashMap;
import java.util.Map;

public class MonkEffect implements Effect {

    private final Map<HouseColor, Integer> students;
    private int cost;

    public MonkEffect() {
        students = new HashMap<>();
    }

    @Override
    public int getId() {
        return 1;
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
        return 1;
    }

    private Map<HouseColor, Integer> getStudents() {
        return students;
    }

    private void addStudent(HouseColor color) {

    }

    private void takeStudent(HouseColor color) {

    }
}
