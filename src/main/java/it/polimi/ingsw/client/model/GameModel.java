package it.polimi.ingsw.client.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.utilities.GameControllerStates;
import it.polimi.ingsw.utilities.WizardType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameModel {
    private final int playersNumber;
    private Map<String, Boolean> waitingRoom;

    private int round;
    private GameControllerStates phase;
    private String subphase;

    private String currentPlayer;
    private boolean expert;
    private List<Player> players;
    private GameBoard gameBoard;


    public GameModel(int expectedPlayers, Map<String, Boolean> waitingRoom) {
        this.waitingRoom = new HashMap<>(waitingRoom);
        this.playersNumber = expectedPlayers;
        this.currentPlayer = null;
    }

    public GameModel(int statusPlayersNumber, int statusRound, GameControllerStates statusPhase, String statusSubphase, boolean statusExpert, String statusCurrentPlayer, JsonArray statusPlayers, JsonObject statusGameBoard) {
        this.waitingRoom = null;
        this.playersNumber = statusPlayersNumber;
        this.round = statusRound;
        this.phase = statusPhase;
        this.subphase = statusSubphase;
        this.expert = statusExpert;
        this.currentPlayer = statusCurrentPlayer;

        this.players = new ArrayList<>();
        for(JsonElement player : statusPlayers){
            String name = player.getAsJsonObject().get("name").getAsString();
            WizardType wizard = WizardType.valueOf(player.getAsJsonObject().get("wizardType").getAsString());
            int coins = player.getAsJsonObject().get("coins").getAsInt();
            JsonArray assistants = player.getAsJsonObject().get("assistants").getAsJsonArray();
            JsonObject schoolBoard = player.getAsJsonObject().get("schoolBoard").getAsJsonObject();

            this.players.add(new Player(name, wizard, coins, schoolBoard, assistants));
        }


        //TODO define game board
        //this.gameBoard = new GameBoard();



    }

    public Map<String, Boolean> getWaitingRoom() {
        return new HashMap<>(this.waitingRoom);
    }

    public int getPlayersNumber(){
        return this.playersNumber;
    }
}
