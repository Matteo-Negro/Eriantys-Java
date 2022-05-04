package it.polimi.ingsw.model.board.effects;

import it.polimi.ingsw.utilities.HouseColor;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Specific effect n.7
 *
 * @author Riccardo Milici
 */

public class JesterEffect extends Effect {

    private Map<HouseColor, Integer> students;


    /**
     * Class constructor.
     * It creates an instance of the class containing a map of the HouseColors of the students and their quantity on the effect card (initialized at 0).
     */
    public JesterEffect() {
        students = new HashMap<>();

        students.put(HouseColor.BLUE, 0);
        students.put(HouseColor.GREEN, 0);
        students.put(HouseColor.FUCHSIA, 0);
        students.put(HouseColor.RED, 0);
        students.put(HouseColor.YELLOW, 0);
    }

    /**
     * Class constructor used to restore the game.
     *
     * @param statusStudents
     */
    public JesterEffect(Map<HouseColor, Integer> statusStudents) {
        this.students = statusStudents;
    }

    @Override
    public int getId() {
        return 7;
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
        return 1;
    }

    /**
     * Returns the map saved in the student attribute.
     *
     * @return student attribute.
     */
    public Map<HouseColor, Integer> getStudents() {
        return students;
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

    /**
     * Standard redefinition of "equals" method.
     *
     * @param o Object to compare.
     * @return true if the two objects are the same.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JesterEffect that = (JesterEffect) o;
        return Objects.equals(students, that.students);
    }

    /**
     * Calculates the hash.
     *
     * @return The calculated hash.
     */
    @Override
    public int hashCode() {
        return Objects.hash(students);
    }
}
