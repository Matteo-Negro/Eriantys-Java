package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.board.effects.*;
import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.Log;

import java.util.Map;
import java.util.Objects;

/**
 * The character card containing the corresponding effect
 *
 * @author Riccardo Milici
 */

public class SpecialCharacter {

    private final int id;
    private final int effectCost;
    private final Effect assignedEffect;
    private boolean alreadyPaid;
    private boolean paidInTurn;
    private boolean isActive;
    private int usesNumber;

    /**
     * Class constructor.
     * It creates an instance of the class containing the given specific effect object and identified by the given numeric id.
     *
     * @param id       The identification number of the special character card.
     * @param students A map with the students to put on the card (null, otherwise).
     */
    public SpecialCharacter(int id, Map<HouseColor, Integer> students) {
        this.id = id;
        isActive = false;
        alreadyPaid = false;
        paidInTurn = false;
        assignedEffect = getEffectBy(id, students, 4);
        effectCost = assignedEffect.getCost();
        usesNumber = switch (this.id) {
            case 7 -> 6;
            case 10 -> 4;
            default -> 1;
        };

        Log.info("*** New SpecialCharacter successfully created with id: " + id);
    }

    /**
     * Class constructor used to restore the game.
     *
     * @param statusId          The identification number of the special character card.
     * @param statusEffectCost  The special character's activation cost.
     * @param statusAlreadyPaid True if the special character has already been paid, and it's effect has already been activated during this game.
     * @param statusPaidInTurn  True if the special character has already been paid, and it's effect has already been activated during this turn.
     * @param statusIsActive    True if the special character's effect is active.
     * @param statusStudents    The students on the card.
     * @param bans              The number of available bans.
     * @param usesNumber        The number of uses.
     */
    public SpecialCharacter(int statusId, int statusEffectCost, boolean statusAlreadyPaid, boolean statusPaidInTurn, boolean statusIsActive, Map<HouseColor, Integer> statusStudents, int bans, int usesNumber) {
        this.id = statusId;
        this.effectCost = statusEffectCost;
        this.assignedEffect = getEffectBy(statusId, statusStudents, bans);
        this.alreadyPaid = statusAlreadyPaid;
        this.paidInTurn = statusPaidInTurn;
        this.isActive = statusIsActive;
        this.usesNumber = usesNumber;

        Log.info("*** Saved SpecialCharacter successfully restored with id: " + statusId);
    }

    /**
     * Gets the effect according to its identification number.
     *
     * @param id The identification number of the effect.
     * @return The required effect.
     */
    private Effect getEffectBy(int id, Map<HouseColor, Integer> students, int bans) {
        return switch (id) {
            case 1 -> new MonkEffect(students);
            case 2 -> new FarmerEffect();
            case 3 -> new HeraldEffect();
            case 4 -> new MessengerEffect();
            case 5 -> new HerbalistEffect(bans);
            case 6 -> new CentaurEffect();
            case 7 -> new JesterEffect(students);
            case 8 -> new KnightEffect();
            case 9 -> new MushroomerEffect();
            case 10 -> new MinstrelEffect();
            case 11 -> new PrincessEffect(students);
            case 12 -> new ThiefEffect();
            default -> throw new IllegalStateException("Unexpected second: " + id);
        };
    }

    /**
     * Returns the identification number of the object.
     *
     * @return id attribute
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the actual cost of the effect assigned to the object: the effect's native cost if it has never been activated; the native cost increased by 1 if it has already been activated.
     *
     * @return effectCost or effectCost+1 attribute
     */
    public int getEffectCost() {
        if (alreadyPaid) {
            return effectCost + 1;
        } else return effectCost;
    }

    /**
     * Returns the instance of the specific effect assigned to the object.
     *
     * @return assignedEffect attribute
     */
    public Effect getEffect() {
        return assignedEffect;
    }

    /**
     * Activates the specific effect assigned to the object.
     */
    public void activateEffect() {
        alreadyPaid = true;
        paidInTurn = true;
        isActive = true;
    }

    /**
     * Deactivates the specific effect assigned to the object.
     */
    public void cleanEffect() {
        isActive = false;
        usesNumber = switch (this.id) {
            case 7 -> 6;
            case 10 -> 4;
            default -> 1;
        };
    }

    /**
     * Sets the "payedInRound" attribute to false.
     */
    public void changedTurn() {
        paidInTurn = false;
    }

    /**
     * Tells if the card had already been paid.
     *
     * @return if the card had already been paid.
     */
    public boolean isAlreadyPaid() {
        return alreadyPaid;
    }

    /**
     * Tells if the card had been paid in the current round.
     *
     * @return if the card had been paid in the current round.
     */
    public boolean isPaidInTurn() {
        return paidInTurn;
    }

    /**
     * Tells if the card is currently active.
     *
     * @return if the card is currently active.
     */
    public boolean isActive() {
        return isActive;
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
     * Increase the counter of uses of the card.
     */
    public void decreaseUsesNumber() {
        usesNumber -= 1;
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
        SpecialCharacter that = (SpecialCharacter) o;
        return id == that.id && effectCost == that.effectCost && alreadyPaid == that.alreadyPaid && paidInTurn == that.paidInTurn && isActive == that.isActive && Objects.equals(assignedEffect, that.assignedEffect);
    }

    /**
     * Calculates the hash.
     *
     * @return The hash-code of the object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, effectCost, assignedEffect, alreadyPaid, paidInTurn, isActive);
    }
}
