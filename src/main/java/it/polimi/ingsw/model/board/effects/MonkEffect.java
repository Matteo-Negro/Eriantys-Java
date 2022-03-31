package it.polimi.ingsw.model.board.effects;

import it.polimi.ingsw.model.utilities.HouseColor;

import java.util.EmptyStackException;
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
    public MonkEffect() {
        students = new HashMap<>();

        students.put(HouseColor.BLUE, 0);
        students.put(HouseColor.GREEN, 0);
        students.put(HouseColor.FUCHSIA, 0);
        students.put(HouseColor.RED, 0);
        students.put(HouseColor.YELLOW, 0);
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
     * Calls the takeStudents(HouseColor) private method.
     * @param color
     */
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

    /**
     * Returns the map saved in the students attribute.
     * @return students attribute.
     */
    private Map<HouseColor, Integer> getStudents() {
        return students;
    }

    /**
     * Increases the counter ,of the color specified by the parameter, in the students' map.
     * @param color
     */
    private void addStudent(HouseColor color) {
        students.replace(color, students.get(color)+1);
    }

    /**
     * Decreases the counter, of the color specified by the parameter, in the students' map.
     * Throws the EmptyStackException if the counter is already at 0.
     * @param color
     * @throws EmptyStackException
     */
    private void takeStudent(HouseColor color) throws EmptyStackException {
        if(students.get(color)==0) throw new EmptyStackException();
        students.replace(color, students.get(color)-1);
    }
}
