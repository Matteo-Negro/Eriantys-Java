package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.GamePlatform;
import it.polimi.ingsw.utilities.HouseColor;

import java.net.Socket;
import java.util.List;

public class GameController {
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

    public String getId() {
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

    public void addUser(Socket userSocket, String name) {

        int userId = 1;

        for (int i = 0; 0 < getUsers().size(); i++) {
            if (userId == getUsers().get(i).getId()) userId++;
        }
        users.add(new User(userId, userSocket, name));
    }

//    public void manageCommand(Map<> command) {
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
