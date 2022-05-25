package it.polimi.ingsw.client.model;

import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.exceptions.IllegalActionException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * The cloud on client-side.
 *
 * @author Riccardo Motta
 */
public class Cloud {
    private List<HouseColor> students;

    /**
     * Default constructor.
     *
     * @param students The students to put on the island.
     */
    public Cloud(Map<HouseColor, Integer> students) {
        this.students = students != null ? parseStudents(students) : null;
    }

    /**
     * Parses a students map in order to obtain a list.
     *
     * @param students The students to parse.
     * @return The generated list.
     */
    private List<HouseColor> parseStudents(Map<HouseColor, Integer> students) {
        List<HouseColor> list = new ArrayList<>();
        for (HouseColor color : HouseColor.values())
            for (int index = 0; index < students.get(color); index++)
                list.add(color);
        if (list.isEmpty())
            return null;
        Collections.shuffle(list);
        return list;
    }

    /**
     * Gets the students on the island, eventually removing them.
     *
     * @param remove true if they have to be removed.
     * @return The list of students.
     */
    public List<HouseColor> getStudents(boolean remove) {
        if (students == null)
            return null;
        if (remove) {
            List<HouseColor> list = new ArrayList<>(students);
            students = null;
            return list;
        }
        return new ArrayList<>(students);
    }

    /**
     * Refills the cloud.
     *
     * @param students The students to put on the island.
     * @throws IllegalActionException Thrown if the cloud is not empty.
     */
    public void refill(Map<HouseColor, Integer> students) throws IllegalActionException {
        if (this.students != null)
            throw new IllegalActionException();
        this.students = parseStudents(students);
    }
}
