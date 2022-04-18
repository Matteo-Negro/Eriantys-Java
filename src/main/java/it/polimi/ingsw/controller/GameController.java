package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GamePlatform;
import it.polimi.ingsw.utilities.HouseColor;

import java.net.Socket;
import java.util.List;

public class GameController extends Thread{
    private final GamePlatform gameModel;
    private String id;
    private int round;
    private String phase;
    private final int expectedPlayers;
    private List<User> users;

    public GameController(GamePlatform gameModel, int expectedPlayers) {
        this.gameModel = gameModel;
        this.expectedPlayers = expectedPlayers;
    }

    public String getGameId() {
        return id;
    }

    public int getRound() {
        return round;
    }

    public String getPhase() {
        return phase;
    }

    public List<User> getUsers() {
        return users;
    }

    public GamePlatform getGameModel() {
        return gameModel;
    }

    public void addUser(User user) {

        int userId = 1;

        for (int i = 0; 0 < getUsers().size(); i++) {
            if (userId == getUsers().get(i).getId()) userId++;
        }
        user.putId(userId);
        users.add(user);
    }

//    public void manageCommand(JsonObject command) {
//
//    }

    public void saveGame() {

    }

    private void playAssistantCard(String player, int assistant) {

    }

    private void moveStudent(HouseColor color, String from, String to) {

    }

    private void moveMotherNature(int moves) {

    }

    private void chooseCloud(int cloud) {

    }

    private void paySpecialCharacter(int specialCharacter) {

    }

//    private boolean checkWinContitions() {
//
//    }

    private void endGame() {

    }
}
