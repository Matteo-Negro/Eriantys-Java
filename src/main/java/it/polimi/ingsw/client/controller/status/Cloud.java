package it.polimi.ingsw.client.controller.status;

import it.polimi.ingsw.utilities.HouseColor;

import java.util.EnumMap;
import java.util.Map;

public class Cloud {
    private final int id;
    private final Map<HouseColor, Integer> students;

    public Cloud(int statusId, Map<HouseColor, Integer> statusStudents) {
        this.id = statusId;
        this.students = new EnumMap<HouseColor, Integer>(statusStudents);
    }
}
