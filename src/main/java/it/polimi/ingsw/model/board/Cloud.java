package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.utilities.HouseColor;

import java.util.Map;

/**
 * Cloud Class
 *
 * @author Matteo Negro
 */

public class Cloud {
    private int id;
    private int studentsNum;
    private Map<HouseColor, Integer> students;

    public Cloud(int idCloud) {
    }

    public int getId() {
        return 0;
    }

    public int getStudentsNum() {
        return 0;
    }

    public boolean isFull() {
        return false;
    }

    public void refill() {
    }

    public Map<HouseColor, Integer> flush() {
        return null;
    }
}
