package it.polimi.ingsw.model.board.effects;

import it.polimi.ingsw.utilities.HouseColor;

import java.util.EmptyStackException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Specific effect n.1
 *
 * @author Riccardo Milici
 */

public class MonkEffect extends Effect {

    private final Map<HouseColor, Integer> students;

    /**
     * Class constructor.
     * It creates an instance of the class containing a map of the students put on the effect card with their respective quantity (initialized at 0).
     */
    public MonkEffect(Map<HouseColor, Integer> studentsStatus) {
        students = new EnumMap<>(studentsStatus);
    }

    @Override
    public int getId() {
        return 1;
    }

    @Override
    public void effect() {

    }

    /**
     * effect() method overload.
     *
     *
     * @param toTake    The color of the desired student.
     * @param toPut     The color of the new student extracted from the bag.
     */
    public void effect(HouseColor toTake, HouseColor toPut) {
        if(toTake != null) takeStudent(toTake);
        if(toPut != null) addStudent(toPut);
    }

    @Override
    public int getCost() {
        return 1;
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
     * Increases the counter ,of the color specified by the parameter, in the students' map.
     *
     * @param color
     */
    private void addStudent(HouseColor color) {
        students.replace(color, students.get(color) + 1);
    }

    /**
     * Decreases the counter, of the color specified by the parameter, in the students' map.
     * Throws the EmptyStackException if the counter is already at 0.
     *
     * @param color
     * @throws EmptyStackException
     */
    private void takeStudent(HouseColor color) throws EmptyStackException {
        if (students.get(color) == 0) throw new EmptyStackException();
        students.replace(color, students.get(color) - 1);
    }
}
