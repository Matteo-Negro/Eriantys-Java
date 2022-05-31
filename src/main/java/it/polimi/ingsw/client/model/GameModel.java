package it.polimi.ingsw.client.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import it.polimi.ingsw.utilities.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameModel {
    private final int playersNumber;
    private final Map<String, Boolean> waitingRoom;

    private int round;
    private Phase phase;
    private GameControllerStates subphase;
    private boolean winner;

    private String currentPlayer;
    private boolean expert;
    private List<Player> players;
    private GameBoard gameBoard;


    public GameModel(int expectedPlayers, Map<String, Boolean> waitingRoom) {
        this.waitingRoom = new HashMap<>(waitingRoom);
        this.playersNumber = expectedPlayers;
        this.currentPlayer = null;
        this.winner = false;
    }

    public GameModel(int statusPlayersNumber, int statusRound, Phase statusPhase, GameControllerStates statusSubphase, boolean statusExpert, String statusCurrentPlayer, JsonArray statusPlayers, JsonObject statusGameBoard) {
        this.waitingRoom = null;
        this.playersNumber = statusPlayersNumber;
        this.round = statusRound;
        this.phase = statusPhase;
        this.subphase = statusSubphase;
        this.expert = statusExpert;
        this.currentPlayer = statusCurrentPlayer;
        this.parsePlayers(statusPlayers, statusGameBoard);
        this.parseGameBoard(statusGameBoard);
        this.winner = false;
    }

    public Map<String, Boolean> getWaitingRoom() {
        if (waitingRoom == null)
            return new HashMap<>();
        return new HashMap<>(waitingRoom);
    }

    public int getRound() {
        return round;
    }

    public boolean isWinner() {
        return winner;
    }

    public int getPlayersNumber() {
        return this.playersNumber;
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public Player getPlayerByName(String name) {
        for (Player player : this.getPlayers()) {
            if (player.getName().equals(name)) return player;
        }
        return null;
    }

    private void parsePlayers(JsonArray players, JsonObject gameBoard) {
        this.players = new ArrayList<>();
        JsonArray playedAssistants = gameBoard.get("playedAssistants").getAsJsonArray();
        Log.error(playedAssistants.toString());
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
            Log.error(String.valueOf(playedAssistant));
            this.players.add(new Player(name, wizard, coins, schoolBoard, assistants, playedAssistant));
        }
        JsonObject professors = gameBoard.get("professors").getAsJsonObject();
        for (String color : professors.keySet()) {
            if (!(professors.get(color) instanceof JsonNull)) {
                this.getPlayerByName(professors.get(color).getAsString()).getSchoolBoard().addProfessor(HouseColor.valueOf(color));
            }
        }
    }

    private void parseGameBoard(JsonObject gameBoard) {
        int motherNatureIsland = gameBoard.get("motherNatureIsland").getAsInt();
        String influenceBonus = null;
        if (!gameBoard.get("influenceBonus").isJsonNull())
            influenceBonus = gameBoard.get("influenceBonus").getAsString();
        HouseColor ignoreColor = null;
        if (!gameBoard.get("ignoreColor").isJsonNull())
            ignoreColor = HouseColor.valueOf(gameBoard.get("ignoreColor").getAsString());
        JsonArray clouds = gameBoard.get("clouds").getAsJsonArray();
        JsonArray islands = gameBoard.get("islands").getAsJsonArray();
        JsonArray specialCharacters = gameBoard.has("characters") ? gameBoard.get("characters").getAsJsonArray() : new JsonArray();

        this.gameBoard = new GameBoard(motherNatureIsland, influenceBonus, ignoreColor, islands, clouds, specialCharacters);
    }

    public boolean isExpert() {
        return this.expert;
    }

    public GameBoard getGameBoard() {
        return this.gameBoard;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public Phase getPhase() {
        return phase;
    }

    public GameControllerStates getSubphase() {
        return subphase;
    }

    public void setCurrentPlayer(String currentPlayer, boolean token) {
        this.currentPlayer = currentPlayer;
        this.getPlayerByName(currentPlayer).setActive(token);
    }

    public void setWinner(boolean winner) {
        this.winner = winner;
    }

    public boolean isMovementEffectActive() {
        if (this.isExpert()) {
            for (SpecialCharacter sc : this.getGameBoard().getSpecialCharacters()) {
                if (sc.isActive() && (sc.getId() == 1 || sc.getId() == 7 || sc.getId() == 11)) return true;
            }
        }
        return false;
    }
}
