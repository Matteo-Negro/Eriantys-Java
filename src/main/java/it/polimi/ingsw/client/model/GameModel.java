package it.polimi.ingsw.client.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameModel {
    private final int playersNumber;
    private final Map<String, Boolean> waitingRoom;
    private final Player currentPlayer;
    private boolean expert;
    private List<Player> players;
    private GameBoard gameBoard;
    private List<SpecialCharacter> specialCharacters;

    public GameModel(int expectedPlayers, Map<String, Boolean> waitingRoom) {
        this.waitingRoom = new HashMap<>(waitingRoom);
        this.playersNumber = expectedPlayers;
        this.currentPlayer = null;
    }

    public GameModel(Map<String, Boolean> statusWaitingRoom, int statusPlayersNumber, boolean statusExpert, Player statusCurrentPlayer, List<Player> statusPlayers, GameBoard statusGameBoard, List<SpecialCharacter> statusSpecialCharacters) {
        this.waitingRoom = statusWaitingRoom;
        this.playersNumber = statusPlayersNumber;
        this.expert = statusExpert;
        this.currentPlayer = statusCurrentPlayer;
        this.players = new ArrayList<>(statusPlayers);
        this.gameBoard = statusGameBoard;
        this.specialCharacters = new ArrayList<>(statusSpecialCharacters);
    }

    public Map<String, Boolean> getWaitingRoom() {
        return new HashMap<>(this.waitingRoom);
    }

    public int getPlayersNumber(){
        return this.playersNumber;
    }
}
