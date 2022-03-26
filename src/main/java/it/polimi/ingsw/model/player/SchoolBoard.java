package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.utilities.HouseColor;
import it.polimi.ingsw.model.utilities.TowerType;

import java.util.Map;

public class SchoolBoard {

    public SchoolBoard() {

    }

    public int getTowersNumber() {
        return 0;
    }

    public TowerType getTowerType() {
        return new TowerType();
    }

    public int getStudentsNumberOf(HouseColor houseColor) {
        return 0;
    }

    public boolean hasProfessorOf(HouseColor houseColor) {
        return false;
    }

    public void addToEntrance(Map<HouseColor, Integer> map) {

    }

    public void removeFromEntrance(HouseColor student) {

    }

    public void addToDiningRoom(HouseColor student) {

    }

    public void addProfessor(HouseColor professor) {

    }

    public void removeProfessor(HouseColor professor) {

    }

    public void removeTowers(int number) {

    }

    public void addTowers(int number) {

    }
}
