package it.polimi.ingsw.client.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.server.model.board.SpecialCharacter;
import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.Log;
import it.polimi.ingsw.utilities.TowerType;
import it.polimi.ingsw.utilities.WizardType;
import it.polimi.ingsw.utilities.exceptions.AlreadyPlayedException;
import it.polimi.ingsw.utilities.exceptions.NegativeException;
import it.polimi.ingsw.utilities.exceptions.NotEnoughCoinsException;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Contains all the data connected to the player on the client side.
 *
 * @author Matteo Negro
 */
public class Player {
    private final String name;
    private final WizardType wizard;
    private final SchoolBoard schoolBoard;
    private final List<Assistant> hand;
    private int coins;
    private boolean active;
    private Assistant currentPlayedAssistant;

    /**
     * Class constructor used to restore the game.
     *
     * @param name        Player's name.
     * @param wizardType  Player's Wizard.
     * @param hand        Player's deck of Assistants.
     * @param coins       Player's coins.
     * @param schoolBoard Player's SchoolBoard.
     */
    public Player(String name, WizardType wizardType, int coins, JsonObject schoolBoard, JsonArray hand) {
        this.name = name;
        this.wizard = wizardType;
        this.coins = coins;

        this.currentPlayedAssistant = null;


        this.hand = new ArrayList<>();
        for(JsonElement assistant : hand){
            int id = assistant.getAsJsonObject().get("id").getAsInt();
            boolean bonus = assistant.getAsJsonObject().get("bonus").getAsBoolean();
            this.hand.add(new Assistant(id, bonus));
        }
        TowerType towerType = TowerType.valueOf(schoolBoard.get("towerType").getAsString());
        int towersNumber = schoolBoard.get("towersNumber").getAsInt();
        Map<HouseColor, Integer> entrance = new EnumMap<>(HouseColor.class);
        for(HouseColor color : HouseColor.values()){
            entrance.put(color, schoolBoard.get("entrance").getAsJsonObject().get(color.toString()).getAsInt());
        }
        Map<HouseColor, Integer> diningRoom = new EnumMap<>(HouseColor.class);
        for(HouseColor color : HouseColor.values()){
            diningRoom.put(color, schoolBoard.get("diningRoom").getAsJsonObject().get(color.toString()).getAsInt());
        }
        //TODO define professors.
        Map<HouseColor, Boolean> professors = new EnumMap<>(HouseColor.class);
        this.schoolBoard = new SchoolBoard(towerType, towersNumber, entrance, diningRoom, professors);
    }

    /**
     * Gets the name of the Player.
     *
     * @return Name of the Player.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the Player's Wizard.
     *
     * @return Player's Wizard.
     */
    public WizardType getWizard() {
        return this.wizard;
    }

    /**
     * Gets the number of coins the Player has.
     *
     * @return Number of coins the Player has.
     */
    public int getCoins() {
        return this.coins;
    }

    /**
     * Adds one coin to preview coin amount.
     */
    public void addCoins() {
        this.coins += 1;
    }

    /**
     * Removes the specified number of coins. Used in combination with paySpecialCharacter.
     *
     * @param number Number of coins to spend.
     * @throws NegativeException       If the number of towers is negative.
     * @throws NotEnoughCoinsException If the number of available coins is less than the required one.
     */
    private void takeCoins(int number) throws NegativeException, NotEnoughCoinsException {
        if (number < 0) throw new NegativeException("Given value is negative (" + number + ")");
        if (this.coins < number)
            throw new NotEnoughCoinsException("Required coins (" + number + ") is more than available (" + this.coins + ")");
        this.coins -= number;
    }

    /**
     * Gets the Player's SchoolBoard.
     *
     * @return Player's SchoolBoard.
     */
    public SchoolBoard getSchoolBoard() {
        return this.schoolBoard;
    }

    /**
     * Gets the list of available assistants in the player's hand.
     *
     * @return List of available assistants.
     */
    public List<Assistant> getHand() {
        return this.hand;
    }

    /**
     * Gets the assistant that was played in the current turn.
     *
     * @return The played assistant.
     */
    public Assistant getCurrentPlayedAssistant() {
        return this.currentPlayedAssistant;
    }

    /**
     * Cleans at the end of the turn the assistant that was played in the current turn.
     */
    public void cleanCurrentPlayedAssitant() {
        this.currentPlayedAssistant = null;
    }

    /**
     * Plays the selected Assistant, giving back a reference to it.
     *
     * @param id ID of the Assistant to be played.
     * @return Chosen Assistant, if available.
     * @throws AlreadyPlayedException If the Assistant had already been played.
     */
    public Assistant playAssistant(int id) throws AlreadyPlayedException {
        if (this.hand.get(id) == null)
            throw new AlreadyPlayedException("The Assistant #" + id + " has already been played.");
        this.currentPlayedAssistant = this.hand.get(id - 1);
        this.hand.set(id - 1, null);
        return this.currentPlayedAssistant;
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
            Log.error(e.getMessage());
        }
        specialCharacter.activateEffect();
    }

    /**
     * Returns the state of the player, true whether it's the current player.
     *
     * @return
     */
    public boolean isActive() {
        return this.active;
    }

    /**
     * Sets the value of active.
     *
     * @param active True whether the player is the current player.
     */
    public void setActive(boolean active) {
        this.active = active;
    }
}