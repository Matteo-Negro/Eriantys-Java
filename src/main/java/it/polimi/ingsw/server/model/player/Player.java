package it.polimi.ingsw.server.model.player;

import it.polimi.ingsw.server.model.board.SpecialCharacter;
import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.Log;
import it.polimi.ingsw.utilities.TowerType;
import it.polimi.ingsw.utilities.WizardType;
import it.polimi.ingsw.utilities.exceptions.AlreadyPlayedException;
import it.polimi.ingsw.utilities.exceptions.NegativeException;
import it.polimi.ingsw.utilities.exceptions.NotEnoughCoinsException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Contains all the data connected to the player.
 *
 * @author Riccardo Motta
 */
public class Player {

    private final String name;
    private final WizardType wizardType;
    private final List<Assistant> assistants;
    private final SchoolBoard schoolBoard;
    private int coins;

    /**
     * Class constructor.
     *
     * @param name         Player's name.
     * @param wizardType   Player's Wizard.
     * @param towersNumber Number of towers to put on the board.
     * @param towerType    Color of the tower.
     * @param entrance     The entrance of the SchoolBoard.
     */
    public Player(String name, WizardType wizardType, int towersNumber, TowerType towerType, Map<HouseColor, Integer> entrance) {
        this.name = name;
        this.wizardType = wizardType;
        this.coins = 1;
        this.assistants = new ArrayList<>();
        for (int index = 1; index <= 10; index++)
            assistants.add(new Assistant(index));
        this.schoolBoard = new SchoolBoard(towersNumber, towerType, entrance);
    }

    /**
     * Class constructor used to restore the game.
     *
     * @param name        Player's name.
     * @param wizardType  Player's Wizard.
     * @param assistants  Player's deck of Assistants.
     * @param coins       Player's coins.
     * @param schoolBoard Player's SchoolBoard.
     */
    public Player(String name, WizardType wizardType, List<Assistant> assistants, int coins, SchoolBoard schoolBoard) {
        this.name = name;
        this.wizardType = wizardType;
        this.assistants = new ArrayList<>(assistants);
        this.coins = coins;
        this.schoolBoard = schoolBoard;
    }

    /**
     * Gets the name of the Player.
     *
     * @return Name of the Player.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the Player's Wizard.
     *
     * @return Player's Wizard.
     */
    public WizardType getWizard() {
        return wizardType;
    }

    /**
     * Gets the number of coins the Player has.
     *
     * @return Number of coins the Player has.
     */
    public int getCoins() {
        return coins;
    }

    /**
     * Adds one coin to preview coin amount.
     */
    public void addCoins() {
        coins += 1;
    }

    /**
     * Removes the specified number of coins. Used in combination with paySpecialCharacter.
     *
     * @param number Number of coins to spend.
     * @throws NegativeException       If the number of towers is negative.
     * @throws NotEnoughCoinsException If the number of available coins is less than the required one.
     */
    private void takeCoins(int number) throws NegativeException, NotEnoughCoinsException {
        if (number < 0) throw new NegativeException("Given second is negative (" + number + ")");
        if (coins < number)
            throw new NotEnoughCoinsException("Required coins (" + number + ") is more than available (" + coins + ")");
        coins -= number;
    }

    /**
     * Gets the list of available Assistants.
     *
     * @return List of available Assistants.
     */
    public List<Assistant> getAssistants() {
        return new ArrayList<>(assistants);
    }

    /**
     * Plays the selected Assistant, giving back a reference to it.
     *
     * @param id ID of the Assistant to be played.
     * @return Chosen Assistant, if available.
     * @throws AlreadyPlayedException If the Assistant had already been played.
     */
    public Assistant playAssistant(int id) throws AlreadyPlayedException {
        if (assistants.get(id - 1) == null)
            throw new AlreadyPlayedException("The Assistant #" + id + " has already been played.");
        Assistant tmp = assistants.get(id - 1);
        assistants.set(id - 1, null);
        return tmp;
    }

    /**
     * Plays the specified SpecialCharacter.
     *
     * @param specialCharacter Selected SpecialCharacter.
     * @throws NotEnoughCoinsException If the number of available coins is less than the required one.
     * @throws NullPointerException    If the argument is null.
     */
    public void paySpecialCharacter(SpecialCharacter specialCharacter) throws NotEnoughCoinsException, NullPointerException {
        if (specialCharacter == null) throw new NullPointerException();
        try {
            takeCoins(specialCharacter.getEffectCost());
        } catch (NegativeException e) {
            Log.warning(e.getMessage());
        }
        specialCharacter.activateEffect();
    }

    /**
     * Gets the Player's SchoolBoard.
     *
     * @return Player's SchoolBoard.
     */
    public SchoolBoard getSchoolBoard() {
        return schoolBoard;
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
        Player player = (Player) o;
        return coins == player.coins && Objects.equals(name, player.name) && wizardType == player.wizardType && Objects.equals(assistants, player.assistants) && Objects.equals(schoolBoard, player.schoolBoard);
    }

    /**
     * Calculates the hash.
     *
     * @return The calculated hash.
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, wizardType, assistants, schoolBoard, coins);
    }
}
