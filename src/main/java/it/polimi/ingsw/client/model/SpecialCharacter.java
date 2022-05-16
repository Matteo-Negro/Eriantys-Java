package it.polimi.ingsw.client.model;

import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.exceptions.IllegalMoveException;

import java.util.EnumMap;
import java.util.Map;

public class SpecialCharacter {
    private final int id;
    //Specialized attributes.
    private final Map<HouseColor, Integer> students;
    private boolean active;
    private boolean alreadyPaid;
    private Integer availableBans;

    public SpecialCharacter(int statusId, boolean statusActive, boolean statusAlreadyPaid, Map<HouseColor, Integer> statusStudents, int statusAvailableBans) {
        this.id = statusId;
        this.active = statusActive;
        this.alreadyPaid = statusAlreadyPaid;
        this.students = new EnumMap<HouseColor, Integer>(statusStudents);
        this.availableBans = statusAvailableBans;
    }

    public int getId() {
        return this.id;
    }

    public boolean isActive() {
        return this.active;
    }

    public boolean isAlreadyPaid() {
        return this.alreadyPaid;
    }

    public Map<HouseColor, Integer> getStudents() {
        return this.students;
    }

    public Integer getAvailableBans() {
        return this.availableBans;
    }

    public void activateEffect() {
        this.active = true;
        this.alreadyPaid = true;
        //update view.
    }

    public void addStudent(HouseColor student) {
        this.students.put(student, this.students.get(student) + 1);
    }

    public void removeStudent(HouseColor student) throws IllegalMoveException {
        if (this.students.get(student) > 0) {
            this.students.put(student, this.students.get(student) - 1);
        } else throw new IllegalMoveException();
    }

    public void addBan() {
        this.availableBans++;
    }

    public void removeBan() throws IllegalMoveException {
        if (this.availableBans > 0) {
            this.availableBans--;
        } else throw new IllegalMoveException();
    }
}
