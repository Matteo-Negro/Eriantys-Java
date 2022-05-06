package it.polimi.ingsw.model.board.effects;

import it.polimi.ingsw.utilities.HouseColor;

import java.util.EmptyStackException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Specific effect n.11
 *
 * @author Riccardo Milici
 */

public class PrincessEffect extends Effect {

    private final Map<HouseColor, Integer> students;

    /**
     * Class constructor used to restore the game.
     *
     * @param statusStudents
     */
    public PrincessEffect(Map<HouseColor, Integer> statusStudents) {
        this.students = statusStudents;
    }

    @Override
    public int getId() {
        return 11;
    }

    @Override
    public void effect() {

    }

    /**
     * effect() method overload.
     *
     *
     * @param toTake
     * @param toPut
     */
    public void effect(HouseColor toTake, HouseColor toPut) {
        if(toTake != null) takeStudent(toTake);
        if(toPut != null) addStudent(toPut);
    }

    @Override
    public int getCost() {
        return 2;
    }

    /**
     * Returns the map saved in the students attribute.
     *
     * @return students attribute.
     */
    public EnumMap<HouseColor, Integer> getStudents() {
        return new EnumMap<>(students);
    }

    /**
     * Adds a student, of the color specified by the parameter, to the map saved in the students attribute, increasing the counter mapped with the respective HouseColor.
     *
     * @param color
     */
    private void addStudent(HouseColor color) {
        students.replace(color, students.get(color) + 1);
    }

    /**
     * Deletes a student, of the color specified by the parameter, from the map saved in the students attribute, decreasing the counter mapped with the respective HouseColor.
     * Throws an EmptyStackException  if the counter is already at 0.
     *
     * @param color
     * @throws EmptyStackException
     */
    private void takeStudent(HouseColor color) throws EmptyStackException {
        if (students.get(color) == 0) throw new EmptyStackException();
        students.replace(color, students.get(color) - 1);
    }
}
