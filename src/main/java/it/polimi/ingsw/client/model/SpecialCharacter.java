package it.polimi.ingsw.client.model;

import com.google.gson.JsonObject;
import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.exceptions.IllegalMoveException;
import it.polimi.ingsw.utilities.parsers.JsonToObjects;

import java.util.EnumMap;
import java.util.Map;

/**
 * The character card on the client side.
 *
 * @author Riccardo Milici
 * @author Matteo Negro
 */
public class SpecialCharacter {
    private final int id;
    private final int cost;
    private Map<HouseColor, Integer> students;
    private boolean active;
    private boolean alreadyPaid;
    private boolean paidInTurn;
    private Integer availableBans;
    private Integer usesNumber;

    /**
     * Class constructor.
     * It creates an instance of the class containing the given specific effect object and identified by the given numeric id.
     *
     * @param id            The identification number of the special character card.
     * @param active        True if the special character's effect is active.
     * @param alreadyPaid   True if the special character has already been paid, and it's effect has already been activated during this game.
     * @param paidInTurn    Tells whether the current card has already been played in the current turn.
     * @param students      A map with the students to put on the card (null, otherwise).
     * @param availableBans Number of ban marker available.
     * @param cost          Price to activate the effect.
     * @param usesNumber    The number of players in the game.
     */
    public SpecialCharacter(int id, boolean active, boolean alreadyPaid, boolean paidInTurn, JsonObject students, Integer availableBans, int cost, int usesNumber) {
        this.id = id;
        this.active = active;
        this.alreadyPaid = alreadyPaid;
        this.paidInTurn = paidInTurn;
        this.availableBans = availableBans;
        this.cost = cost;
        this.students = null;
        this.usesNumber = usesNumber;
        if (students != null) this.students = JsonToObjects.parseStudents(students);
    }

    /**
     * Increase the counter of uses of the card.
     */
    public void decreaseUsesNumber() {
        this.usesNumber -= 1;
    }

    /**
     * Tells the number of uses of the card in this turn.
     *
     * @return The number of uses of the card in this turn.
     */
    public Integer getUsesNumber() {
        return this.usesNumber;
    }

    /**
     * Returns the identification number of the object.
     *
     * @return Id attribute.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Tells if the card is currently active.
     *
     * @return if the card is currently active.
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     * Tells if the card had already been paid.
     *
     * @return if the card had already been paid.
     */
    public boolean isAlreadyPaid() {
        return this.alreadyPaid;
    }

    /**
     * Returns the map saved in the students attribute.
     *
     * @return students attribute.
     */
    public Map<HouseColor, Integer> getStudents() {
        if (this.students != null) return new EnumMap<>(this.students);
        return null;
    }

    /**
     * Returns the availableBans attribute.
     *
     * @return availableBans attribute.
     */
    public Integer getAvailableBans() {
        return this.availableBans;
    }

    /**
     * Returns true if this character has already been paid during this round.
     *
     * @return The paidInRound attribute.
     */
    public boolean isPaidInTurn() {
        return this.paidInTurn;
    }

    /**
     * Activates the specific effect assigned to the object.
     */
    public void activateEffect() {
        this.active = true;
        this.alreadyPaid = true;
        this.paidInTurn = true;
        //update view.
    }

    /**
     * Adds a student, of the color specified by the parameter, to the map saved in the students attribute, increasing the counter mapped with the respective HouseColor.
     *
     * @param student The color of the students to increase.
     */
    public void addStudent(HouseColor student) {
        this.students.put(student, this.students.get(student) + 1);
    }

    /**
     * Deletes a student, of the color specified by the parameter, from the map saved in the students attribute, decreasing the counter mapped with the respective HouseColor.
     *
     * @param student The color of the students to decrease.
     * @throws IllegalMoveException Thrown when there are no students of the specified color on the card.
     */
    public void removeStudent(HouseColor student) throws IllegalMoveException {
        if (this.students.get(student) > 0) {
            this.students.put(student, this.students.get(student) - 1);
        } else throw new IllegalMoveException();
    }

    /**
     * Increases the number of the available bans.
     */
    public void addBan() {
        this.availableBans++;
    }

    /**
     * Decreases the number of the available bans.
     *
     * @throws IllegalMoveException Thrown when there are no ban marker on the card.
     */
    public void removeBan() throws IllegalMoveException {
        if (this.availableBans > 0) {
            this.availableBans--;
        } else throw new IllegalMoveException();
    }

    /**
     * Returns the activation cost of the effect.
     *
     * @return cost attribute
     */
    public int getCost() {
        return (this.alreadyPaid) ? this.cost + 1 : this.cost;
    }
}
