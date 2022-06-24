package it.polimi.ingsw.server.model.board.effects;

import it.polimi.ingsw.utilities.HouseColor;

import java.util.EmptyStackException;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * Specific effect n.1.
 *
 * @author Riccardo Milici
 */
public class MonkEffect implements Effect {

    private final Map<HouseColor, Integer> students;

    /**
     * Class constructor.
     * It creates an instance of the class containing a map of the students put on the effect card with their respective quantity (initialized at 0).
     */
    public MonkEffect() {
        students = new EnumMap<>(HouseColor.class);

        students.put(HouseColor.BLUE, 0);
        students.put(HouseColor.GREEN, 0);
        students.put(HouseColor.FUCHSIA, 0);
        students.put(HouseColor.RED, 0);
        students.put(HouseColor.YELLOW, 0);
    }

    /**
     * Class constructor used to restore the game.
     *
     * @param studentsStatus Indicates the student on the card, saved into the status. These are going to be stored as a Map into the students attribute.
     */
    public MonkEffect(Map<HouseColor, Integer> studentsStatus) {
        students = new EnumMap<>(studentsStatus);
    }

    /**
     * Returns the identification number of the specific effect object.
     *
     * @return id attribute.
     */
    @Override
    public int getId() {
        return 1;
    }

    /**
     * The effect of the card.
     *
     * @param toTake The color of the student to take from the card.
     * @param toPut  The color of the student to put on the card.
     */
    public void effect(HouseColor toTake, HouseColor toPut) {
        if (toTake != null)
            takeStudent(toTake);
        if (toPut != null)
            addStudent(toPut);
    }

    /**
     * Returns the activation cost of the effect.
     *
     * @return cost attribute.
     */
    @Override
    public int getCost() {
        return 1;
    }

    /**
     * Returns the map saved in the students attribute.
     *
     * @return students attribute.
     */
    public Map<HouseColor, Integer> getStudents() {
        return new EnumMap<>(students);
    }

    /**
     * Increases the counter ,of the color specified by the parameter, in the students' map.
     *
     * @param color The color of the students to increase.
     */
    private void addStudent(HouseColor color) {
        students.replace(color, students.get(color) + 1);
    }

    /**
     * Decreases the counter, of the color specified by the parameter, in the students' map.
     * Throws the EmptyStackException if the counter is already at 0.
     *
     * @param color The color of the students to decrease.
     * @throws EmptyStackException Thrown when there are no students of the specified color on the card.
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
        MonkEffect that = (MonkEffect) o;
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
