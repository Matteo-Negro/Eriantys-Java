package it.polimi.ingsw.model;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.utilities.WizardType;

import java.util.ArrayList;
import java.util.List;

public class GamePlatform {

    public GamePlatform(String id) {

    }

    public String getId() {
        return "";
    }

    public int getPlayersNumber() {
        return 0;
    }

    public List<Player> getPlayers() {
        return new ArrayList<>();
    }

    public void addPlayer(String id, WizardType wizardType) {

    }

    public Player getPlayerByName(String name) {
        return new Player(name);
    }

    public boolean isExpert() {
        return false;
    }

    public String getCurrentPlayer() {
        return "";
    }

    public String getRoundWinner() {
        return "";
    }

    public void nextTurn() {

    }

    public void nextRound() {

    }

    public GameBoard getGameBoard() {
        return new GameBoard();
    }

    private void updateTurnOrder() {

    }
}
