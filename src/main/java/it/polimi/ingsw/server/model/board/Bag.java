package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.Log;

import java.util.*;

/**
 * Bag Class, it's one of GameBoard elements, contains all the students before the deployment.
 *
 * @author Matteo Negro
 */

public class Bag {
    private final Stack<HouseColor> listStudents;
    Map<HouseColor, Integer> result;

    /**
     * Bag Constructor, listStudents is initialized with the all 120 students and shuffled.
     */
    public Bag() {
        this.listStudents = new Stack<>();
        this.result = new EnumMap<>(HouseColor.class);

        Arrays.stream(HouseColor.values()).forEach(color -> {
            for (int i = 0; i < 24; i++) {
                this.listStudents.push(color);
            }
        });

        this.randomize();

        Log.debug("*** New Bag successfully created.");
    }

    /**
     * Bag Constructor, listStudents is initialized with the remaining students to restart the Game from the last state.
     *
     * @param status Last state of the bag, it's a map that contains the number of student for each house color.
     */
    public Bag(Map<HouseColor, Integer> status) {
        this.listStudents = new Stack<>();
        this.result = new EnumMap<>(HouseColor.class);

        Arrays.stream(HouseColor.values()).forEach(color -> {
            for (int i = 0; i < status.get(color); i++) {
                this.listStudents.push(color);
            }
        });

        this.randomize();

        Log.debug("*** Saved Bag successfully restored.");
    }

    /**
     * This method gets and removes the first element in the List.
     *
     * @return First element in the List.
     * @throws EmptyStackException The listStudents is empty.
     */
    public HouseColor pop() throws EmptyStackException {
        return this.listStudents.pop();
    }

    /**
     * This method adds a student in the List and shuffles it.
     *
     * @param student The student (HouseColor) that the user would like to put back in the bag.
     */
    public void push(HouseColor student) {
        this.listStudents.push(student);

        this.randomize();
    }

    /**
     * This method gives as result an arraylist to fill the GambeBoard the first time.
     *
     * @return The arraylist with the sequence of students for the setup.
     */
    public List<HouseColor> boardSetUp() {
        ArrayList<HouseColor> setUp = new ArrayList<>();
        Arrays.stream(HouseColor.values()).forEach(color -> {
            setUp.add(color);
            setUp.add(color);
        });

        Collections.shuffle(setUp);
        return setUp;
    }

    /**
     * This method returns the present state of the bag.
     *
     * @return A map that contains the number of students for each color.
     */
    public Map<HouseColor, Integer> getStatus() {
        listStudents.forEach(color -> {
            if (result.containsKey(color)) {
                result.put(color, result.get(color) + 1);
            } else {
                result.put(color, 0);
            }
        });

        return result;
    }

    /**
     * This method returns whether the bag is empty.
     *
     * @return True whether the bag is empty.
     */
    public boolean isEmpty() {
        return this.listStudents.isEmpty();
    }

    /**
     * This method shuffles the students in the List.
     */
    private void randomize() {
        Collections.shuffle(this.listStudents);
    }
}