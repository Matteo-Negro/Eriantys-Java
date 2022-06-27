package it.polimi.ingsw.client.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import it.polimi.ingsw.utilities.GameControllerState;
import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.Phase;
import it.polimi.ingsw.utilities.WizardType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the main class which links all the client's game model classes.
 *
 * @author Riccardo Milici
 */
public class GameModel {
    private final int playersNumber;
    private final Map<String, Boolean> waitingRoom;

    private int round;
    private Phase phase;
    private GameControllerState subPhase;

    private String currentPlayer;
    private boolean expert;
    private List<Player> players;
    private GameBoard gameBoard;

    /**
     * The class constructor used during the waiting-room state management.
     *
     * @param expectedPlayers The game players number.
     * @param waitingRoom     A map containing the players currently logged into the game and waiting for it to start.
     */
    public GameModel(int expectedPlayers, Map<String, Boolean> waitingRoom) {
        this.waitingRoom = new HashMap<>(waitingRoom);
        this.playersNumber = expectedPlayers;
        this.currentPlayer = null;
    }

    /**
     * The class constructor used to initialize and update the game model when it's running.
     *
     * @param statusPlayersNumber The game players number.
     * @param statusRound         The number of the current round.
     * @param statusPhase         The current game phase.
     * @param statusSubphase      The current game sub-phase.
     * @param statusExpert        A boolean parameter indicating the game difficulty.
     * @param statusCurrentPlayer The current player.
     * @param statusPlayers       A JsonArray containing the info about the players.
     * @param statusGameBoard     A JsonObject containing the info about the game board.
     */
    public GameModel(int statusPlayersNumber, int statusRound, Phase statusPhase, GameControllerState statusSubphase, boolean statusExpert, String statusCurrentPlayer, JsonArray statusPlayers, JsonObject statusGameBoard) {
        this.waitingRoom = null;
        this.playersNumber = statusPlayersNumber;
        this.round = statusRound;
        this.phase = statusPhase;
        this.subPhase = statusSubphase;
        this.expert = statusExpert;
        this.currentPlayer = statusCurrentPlayer;
        this.parsePlayers(statusPlayers, statusGameBoard);
        this.parseGameBoard(statusGameBoard);
    }

    /**
     * Gets a map containing the players registered into the game and their current connection status.
     *
     * @return A new HashMap containing a copy of the waitingRoom attribute.
     */
    public Map<String, Boolean> getWaitingRoom() {
        if (waitingRoom == null)
            return new HashMap<>();
        return new HashMap<>(waitingRoom);
    }

    /**
     * Gets the current game round.
     *
     * @return The round attribute.
     */
    public int getRound() {
        return round;
    }

    /**
     * Gets the total number of the players in this game.
     *
     * @return The playersNumber attribute.
     */
    public int getPlayersNumber() {
        return this.playersNumber;
    }

    /**
     * Gets a list of the players in this game.
     *
     * @return The players attribute.
     */
    public List<Player> getPlayers() {
        return this.players;
    }

    /**
     * Gets the player having the specified username.
     *
     * @param name The name of the player to search.
     * @return The player searched if present, null otherwise.
     */
    public Player getPlayerByName(String name) {
        for (Player player : this.getPlayers()) {
            if (player.getName().equals(name)) return player;
        }
        return null;
    }

    /**
     * This method instantiates and initializes the players through the info contained into the given JsonArray.
     *
     * @param players   The JsonArray containing the players info.
     * @param gameBoard The JsonObject containing the game board info.
     */
    private void parsePlayers(JsonArray players, JsonObject gameBoard) {
        this.players = new ArrayList<>();
        JsonArray playedAssistants = gameBoard.get("playedAssistants").getAsJsonArray();
        for (JsonElement player : players) {
            String name = player.getAsJsonObject().get("name").getAsString();
            WizardType wizard = WizardType.valueOf(player.getAsJsonObject().get("wizardType").getAsString());
            int coins = player.getAsJsonObject().get("coins").getAsInt();
            JsonArray assistants = player.getAsJsonObject().get("assistants").getAsJsonArray();
            JsonObject schoolBoard = player.getAsJsonObject().get("schoolBoard").getAsJsonObject();
            JsonObject playedAssistant = null;
            for (JsonElement assistant : playedAssistants) {
                if (assistant.getAsJsonObject().get("player").getAsString().equals(player.getAsJsonObject().get("name").getAsString()))
                    playedAssistant = assistant.getAsJsonObject();
            }
            this.players.add(new Player(name, wizard, coins, schoolBoard, assistants, playedAssistant));
        }
        JsonObject professors = gameBoard.get("professors").getAsJsonObject();
        for (String color : professors.keySet()) {
            if (!(professors.get(color) instanceof JsonNull)) {
                this.getPlayerByName(professors.get(color).getAsString()).getSchoolBoard().addProfessor(HouseColor.valueOf(color));
            }
        }
    }

    /**
     * This method instantiates and initializes the game board through the info contained into the given JsonObject.
     *
     * @param gameBoard The JsonObject containing the game board info.
     */
    private void parseGameBoard(JsonObject gameBoard) {
        String influenceBonus = null;
        if (!gameBoard.get("influenceBonus").isJsonNull())
            influenceBonus = gameBoard.get("influenceBonus").getAsString();
        HouseColor ignoreColor = null;
        if (!gameBoard.get("ignoreColor").isJsonNull())
            ignoreColor = HouseColor.valueOf(gameBoard.get("ignoreColor").getAsString());
        JsonArray clouds = gameBoard.get("clouds").getAsJsonArray();
        JsonArray islands = gameBoard.get("islands").getAsJsonArray();
        JsonArray specialCharacters = gameBoard.has("characters") ? gameBoard.get("characters").getAsJsonArray() : new JsonArray();

        this.gameBoard = new GameBoard(gameBoard.get("motherNatureIsland").getAsInt(), influenceBonus, ignoreColor, islands, clouds, specialCharacters);
    }

    /**
     * Tells the game difficulty.
     *
     * @return True if this game is expert, false otherwise.
     */
    public boolean isExpert() {
        return this.expert;
    }

    /**
     * Gets the game board.
     *
     * @return The gameBoard attribute.
     */
    public GameBoard getGameBoard() {
        return this.gameBoard;
    }

    /**
     * Gets the current player.
     *
     * @return The currentPlayer attribute.
     */
    public String getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Gets the current game phase.
     *
     * @return The phase attribute.
     */
    public Phase getPhase() {
        return phase;
    }

    /**
     * Gets the current game sub-phase.
     *
     * @return The subphase attribute.
     */
    public GameControllerState getSubPhase() {
        return subPhase;
    }

    /**
     * Sets the current player and activates/deactivates it's input, receiving the communication token.
     *
     * @param currentPlayer The username of the current player.
     * @param token         The boolean second associated with the communication token.
     */
    public void setCurrentPlayer(String currentPlayer, boolean token) {
        this.currentPlayer = currentPlayer;
        this.getPlayerByName(currentPlayer).setActive(token);
    }

    /**
     * Tells if a special character's effect is active.
     *
     * @return True if an effect is currently active, false otherwise.
     */
    public boolean isEffectActive() {
        if (this.isExpert()) {
            for (SpecialCharacter sc : this.getGameBoard().getSpecialCharacters()) {
                if (sc.isActive()) return true;
            }
        }
        return false;
    }
}
