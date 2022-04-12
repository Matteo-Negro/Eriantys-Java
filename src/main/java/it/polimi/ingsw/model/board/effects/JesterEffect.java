package it.polimi.ingsw.model.board.effects;

import it.polimi.ingsw.model.utilities.HouseColor;

import java.util.HashMap;
import java.util.Map;

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
     * Calls the exchangeStudents(Map<HouseColor, Integer> exchangedStudents) private method.
     *
     * @param exchangedStudents
     */
    public void effect(Map<HouseColor, Integer> exchangedStudents) {
        exchangeStudents(exchangedStudents);
    }

    @Override
    public void clean() {
        students = new HashMap<>();
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
    private Map<HouseColor, Integer> getStudents() {
        return students;
    }

    /**
     * Overwrites the mapping saved in the students attribute with the mapping of the students exchanged.
     *
     * @param exchangedStudents
     */
    private void exchangeStudents(Map<HouseColor, Integer> exchangedStudents) {
        for (HouseColor key : exchangedStudents.keySet()) {
            students.replace(key, students.get(key) + exchangedStudents.get(key));
        }
    }
}
