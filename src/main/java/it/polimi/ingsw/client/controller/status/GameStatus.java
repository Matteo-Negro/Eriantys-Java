package it.polimi.ingsw.client.controller.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameStatus {
    private final int playersNumber;
    private final Map<String, String> waitingRoom;
    private boolean expert;
    private final Player currentPlayer;
    private List<Player> players;
    private GameBoard gameBoard;
    private List<SpecialCharacter> specialCharacters;

    public GameStatus(int expectedPlayers, Map<String, String> waitingRoom) {
        this.waitingRoom = new HashMap<>(waitingRoom);
        this.playersNumber = expectedPlayers;
        this.currentPlayer = null;
    }

    public GameStatus(int statusPlayersNumber, boolean statusExpert, Player statusCurrentPlayer, List<Player> statusPlayers, GameBoard statusGameBoard, List<SpecialCharacter> statusSpecialCharacters) {
        this.waitingRoom = null;
        this.playersNumber = statusPlayersNumber;
        this.expert = statusExpert;
        this.currentPlayer = statusCurrentPlayer;
        this.players = new ArrayList<>(statusPlayers);
        this.gameBoard = statusGameBoard;
        this.specialCharacters = new ArrayList<>(statusSpecialCharacters);
    }

    public Map<String, String> getWaitingRoom() {
        return new HashMap<>(this.waitingRoom);
    }
}
