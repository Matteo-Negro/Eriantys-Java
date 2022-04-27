package it.polimi.ingsw.controller;

import com.google.gson.JsonObject;
import it.polimi.ingsw.model.GamePlatform;
import it.polimi.ingsw.model.board.Island;
import it.polimi.ingsw.model.board.SpecialCharacter;
import it.polimi.ingsw.model.board.effects.MonkEffect;
import it.polimi.ingsw.model.player.Assistant;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.MessageCreator;
import it.polimi.ingsw.utilities.exceptions.*;
import it.polimi.ingsw.utilities.parsers.ObjectsToJson;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

/**
 * GameController
 *
 * @author Matteo Negro
 * @author Riccardo Motta
 */
public class GameController extends Thread {
    private int connectedPlayers;
    private final int expectedPlayers;
    private final GamePlatform gameModel;
    private String id;
    private String phase;
    private int round;
    private final String savePath;
    private final Map<String, User> users;

    /**
     * The game controller constructor.
     *
     * @param gameModel       The reference to the game model, that represent the game state.
     * @param expectedPlayers The number of expected player.
     * @param savePath        The path to the location where the game will be saved.
     */
    public GameController(GamePlatform gameModel, int expectedPlayers, String savePath) {
        this.connectedPlayers = 0;
        this.expectedPlayers = expectedPlayers;
        this.gameModel = gameModel;
        //this.id=null;
        //this.phase=null;
        this.round = 0;
        this.savePath = savePath;
        this.users = new HashMap<>();
    }

    /**
     * This method returns the number of the player that will play the game.
     *
     * @return The number of expected player.
     */
    public int getExpectedPlayers() {
        return this.expectedPlayers;
    }

    /**
     * This method returns the reference to the game model.
     *
     * @return The reference to the game model, that represent the game state.
     */
    public GamePlatform getGameModel() {
        return this.gameModel;
    }

    /**
     * This method returns an alphanumeric id of the game.
     *
     * @return The game's id.
     */
    public String getGameId() {
        return this.id;
    }

    /**
     * This method returns the phase of the turn.
     *
     * @return The phase of the turn.
     */
    public String getPhase() {
        return this.phase;
    }

    /**
     * This method returns the round number.
     *
     * @return The number of the round that is playing.
     */
    public int getRound() {
        return this.round;
    }

    /**
     * This method returns the path where the game will be saved.
     *
     * @return The path to the location used to save the game.
     */
    public String getSavePath() {
        return this.savePath;
    }

    /**
     * This method returns the active users in the game.
     *
     * @return The set of the users that are online.
     */
    public Set<User> getUsers() {
        synchronized (this.users) {
            return new HashSet<>(this.users.values());
        }
    }

    /**
     * This method returns the user associated to the name in the parameter.
     *
     * @param name
     * @return
     */
    public User getUser(String name) {
        synchronized (this.users) {
            return this.users.get(name);
        }
    }

    /**
     * @return
     */
    public Set<String> getUsernames() {
        synchronized (this.users) {
            return this.users.keySet();
        }
    }

    /**
     * @param name
     * @param user
     * @throws FullGameException
     * @throws AlreadyExistingPlayerException
     */
    public void addUser(String name, User user) throws FullGameException, AlreadyExistingPlayerException {
        if (isFull())
            throw new FullGameException();

        synchronized (this.users) {
            if (this.users.get(name) != null)
                throw new AlreadyExistingPlayerException();

            this.users.put(name, user);
            this.connectedPlayers++;

            if (this.gameModel.getPlayers().stream().noneMatch(player -> player.getName().equals(name)))
                this.gameModel.addPlayer(name);
        }

        // TODO: notify game start if full
    }

    /**
     * @param user
     */
    public void removeUser(User user) {
        synchronized (this.users) {
            if (user.getUsername() == null)
                return;
            this.users.replace(user.getUsername(), null);
            this.connectedPlayers--;
            // TODO: pause game (notifyAll())
        }
    }

    /**
     * @return
     */
    public boolean isFull() {
        synchronized (this.users) {
            return this.connectedPlayers == this.expectedPlayers;
        }
    }

    /**
     * Saves the current state of the game into a file.
     */
    private void saveGame() {

        JsonObject json = new JsonObject();
        json.addProperty("id", this.id);
        json.addProperty("expert", this.gameModel.isExpert());
        json.addProperty("currentPlayer", this.gameModel.getCurrentPlayer().getName());
        json.add("clockwiseOrder", ObjectsToJson.toJsonArray(this.gameModel.getPlayers(), ObjectsToJson.GET_NAMES));
        json.add("turnOrder", ObjectsToJson.toJsonArray(this.gameModel.getTurnOrder(), ObjectsToJson.GET_NAMES));
        json.addProperty("expectedPlayers", this.expectedPlayers);
        json.add("players", ObjectsToJson.toJsonArray(this.gameModel.getPlayers(), ObjectsToJson.GET_PLAYERS));
        json.add("board", ObjectsToJson.toJsonObject(this.gameModel.getGameBoard()));

        new Thread(() -> {
            try {
                writeFile(json);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    /**
     * Writes the json containing the state into a file.
     *
     * @param json The json to write.
     * @throws IOException If there is an exception during the process.
     */
    private void writeFile(JsonObject json) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(
                Paths.get(this.savePath, this.id + ".json"),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE);
        writer.write(json.toString());
        writer.close();
    }

    private void playAssistantCard(String player, int assistant) {
        try {
            this.gameModel.getPlayerByName(player).playAssistant(assistant);
        } catch (AlreadyPlayedException e) {
            //TODO: send message
            e.printStackTrace();
        }

        try {
            this.gameModel.getGameBoard().addPlayedAssistant(this.gameModel.getPlayerByName(player), new Assistant(assistant));
        } catch (IllegalMoveException e) {
            //TODO: send message
            e.printStackTrace();
        }
    }

    private void moveStudent(JsonObject command) {
        try {
            switch (command.get("from").getAsString()) {
                case "entrance" ->
                        this.gameModel.getPlayerByName(command.get("player").getAsString()).getSchoolBoard().removeFromEntrance(HouseColor.valueOf(command.get("color").getAsString()));
                case "diningRoom" -> {
                    String newProfessorOwner = this.gameModel.getGameBoard().getProfessors().get(HouseColor.valueOf(command.get("color").getAsString())).getName();
                    int numStudent;
                    this.gameModel.getPlayerByName(command.get("player").getAsString()).getSchoolBoard().removeFromDiningRoom(HouseColor.valueOf(command.get("color").getAsString()));
                    numStudent = this.gameModel.getPlayerByName(command.get("player").getAsString()).getSchoolBoard().getStudentsNumberOf(HouseColor.valueOf(command.get("color").getAsString()));
                    for (Player p : this.gameModel.getPlayers()) {
                        if (numStudent < p.getSchoolBoard().getStudentsNumberOf(HouseColor.valueOf(command.get("color").getAsString()))) {
                            numStudent = p.getSchoolBoard().getStudentsNumberOf(HouseColor.valueOf(command.get("color").getAsString()));
                            newProfessorOwner = p.getName();
                        }
                    }
                    this.gameModel.getGameBoard().getProfessors().put(HouseColor.valueOf(command.get("color").getAsString()), this.gameModel.getPlayerByName(newProfessorOwner));
                }
                case "card" -> {
                    boolean check = false;
                    for (SpecialCharacter c : this.gameModel.getGameBoard().getCharacters()) {
                        if (c.getId() == 1 && c.isActive()) {
                            //TODO: check for Exception??
                            ((MonkEffect) c.getEffect()).effect(HouseColor.valueOf(command.get("color").getAsString()));
                            check = true;
                            break;
                        } else if (c.getId() == 7 && c.isActive()) {
                            //TODO: Effect 7 (Jester)
                        }
                    }
                    if (!check) {
                        //TODO: send it
                        MessageCreator.error("Error: card not available");
                    }
                }
                default ->
                    //TODO: send it
                        MessageCreator.error("Wrong command.");
            }
        } catch (NoStudentException e) {
            e.printStackTrace();
        }

        switch (command.get("to").getAsString()) {
            case "diningRoom" -> {
                this.gameModel.getPlayerByName(command.get("player").getAsString()).getSchoolBoard().addToDiningRoom(HouseColor.valueOf(command.get("color").getAsString()));
                if (this.gameModel.isExpert()) {
                    if (this.gameModel.getPlayerByName(command.get("player").getAsString()).getSchoolBoard().getStudentsNumberOf(HouseColor.valueOf(command.get("color").getAsString())) % 3 == 0) {
                        this.gameModel.getPlayerByName(command.get("player").getAsString()).addCoins();
                    }
                }
            }
            case "bag" ->
                    this.gameModel.getGameBoard().getBag().push(HouseColor.valueOf(command.get("color").getAsString()));
            case "island" ->
                    this.gameModel.getGameBoard().getIslands().get(command.get("toId").getAsInt()).addStudent(HouseColor.valueOf(command.get("color").getAsString()));
            case "card" -> {
                boolean check = false;
                for (SpecialCharacter c : this.gameModel.getGameBoard().getCharacters()) {
                    if (c.getId() == 1 && c.isActive()) {
                        ((MonkEffect) c.getEffect()).addStudent(HouseColor.valueOf(command.get("color").getAsString()));
                        check = true;
                        break;
                    } else if (c.getId() == 7 && c.isActive()) {

                    }
                }
                if (!check) {
                    //TODO: send it
                    MessageCreator.error("Error: card not available");
                }
            }
            default ->
                //TODO: send it
                    MessageCreator.error("Wrong command.");
        }
    }

    private void moveMotherNature(int idIsland) {
        Map<Player, Integer> influence;
        Island island = null;
        try {
            island = this.gameModel.getGameBoard().getIslandById(idIsland);
            this.gameModel.getGameBoard().moveMotherNature(island,
                    this.gameModel.getGameBoard().getPlayedAssistants().get(this.gameModel.getCurrentPlayer()));

        } catch (IllegalMoveException | IslandNotFoundException e) {
            //TODO: send it
            MessageCreator.error("Error");
            e.printStackTrace();
        }
        if (island != null) {
            influence = this.gameModel.getGameBoard().getInfluence(island);
            int max = Collections.max(influence.values());
            List<Player> mostInfluential = influence.entrySet().stream()
                    .filter(entry -> entry.getValue() == max)
                    .map(entry -> entry.getKey())
                    .collect(Collectors.toList());
            if (mostInfluential.size() == 1 || (mostInfluential.size() == 2 && mostInfluential.get(0).getSchoolBoard().getTowerType().equals(mostInfluential.get(1).getSchoolBoard().getTowerType()))) {
                if (island.getTower() == null) {
                    island.setTower(mostInfluential.get(0).getSchoolBoard().getTowerType());
                    try {
                        for (Player player : this.gameModel.getPlayers()) {
                            if (player.getSchoolBoard().getTowerType().equals(mostInfluential.get(0).getSchoolBoard().getTowerType())) {
                                player.getSchoolBoard().removeTowers(island.getSize());
                            }
                        }
                    } catch (NotEnoughTowersException e1) {
                        //TODO: Check for it
                        endGame();
                    } catch (NegativeException e2) {
                        MessageCreator.error("Error");
                        e2.printStackTrace();
                    }
                } else {
                    if (!island.getTower().equals(mostInfluential.get(0).getSchoolBoard().getTowerType())) {
                        try {
                            for (Player player : this.gameModel.getPlayers()) {
                                if (island.getTower().equals(player.getSchoolBoard().getTowerType())) {
                                    player.getSchoolBoard().addTowers(island.getSize());
                                } else if (player.getSchoolBoard().getTowerType().equals(mostInfluential.get(0).getSchoolBoard().getTowerType())) {
                                    player.getSchoolBoard().removeTowers(island.getSize());
                                }
                            }
                            island.setTower(mostInfluential.get(0).getSchoolBoard().getTowerType());
                        } catch (NotEnoughTowersException e1) {
                            //TODO: Check for it
                            endGame();
                        } catch (NegativeException e2) {
                            MessageCreator.error("Error");
                            e2.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private void chooseCloud(JsonObject command) {
        this.gameModel.getPlayerByName(command.get("player").getAsString()).getSchoolBoard().addToEntrance(this.gameModel.getGameBoard().getClouds().get(command.get("cloud").getAsInt()).flush());
    }

    private void paySpecialCharacter(int specialCharacter) {
        this.gameModel.getGameBoard().getCharacters().get(specialCharacter).activateEffect();
    }

    private boolean endGame() {
        boolean end = false;
        List<Player> winners = new ArrayList<>();
        if (this.gameModel.getPlayers().stream().anyMatch(player -> player.getSchoolBoard().getTowersNumber() == 0)) {
            end = true;
            for (Player player : this.gameModel.getPlayers()) {
                if (player.getSchoolBoard().getTowersNumber() == 0) winners.add(player);
            }
        } else if (this.gameModel.getGameBoard().getIslands().size() == 3) {
            //TODO
            end = checkForWinners();
        } else if ((this.gameModel.getCurrentPlayer().equals(this.gameModel.getTurnOrder().get(this.expectedPlayers - 1)))) {
            if (this.gameModel.getGameBoard().getBag().isEmpty() || this.gameModel.getPlayers().get(0).getAssistants().size() == 0) {
                end = checkForWinners();
            }
        }
        return end;
    }

    private boolean checkForWinners() {
        boolean end = false;
        List<Player> winners = new ArrayList<>();

        Map<Player, Integer> numTowerPlayers = new HashMap<>();
        for (Player player : this.gameModel.getPlayers()) {
            numTowerPlayers.put(player, player.getSchoolBoard().getTowersNumber());
        }
        int min = Collections.min(numTowerPlayers.values());
        List<Player> possibleWinners = numTowerPlayers.entrySet().stream()
                .filter(entry -> entry.getValue() == min)
                .map(entry -> entry.getKey())
                .collect(Collectors.toList());
        if (possibleWinners.size() == 1 || (possibleWinners.size() == 2 && possibleWinners.get(0).getSchoolBoard().getTowerType().equals(possibleWinners.get(1).getSchoolBoard().getTowerType()))) {
            winners.addAll(possibleWinners);
            end = true;
        } else {
            Map<Player, Integer> numProfessorsPlayers = new HashMap<>();
            Player player;
            for (HouseColor color : HouseColor.values()) {
                player = this.gameModel.getGameBoard().getProfessors().get(color);
                numProfessorsPlayers.put(player, numProfessorsPlayers.containsKey(player) ? numProfessorsPlayers.get(player) + 1 : 1);
            }
            int max = Collections.max(numProfessorsPlayers.values());
            possibleWinners = numProfessorsPlayers.entrySet().stream()
                    .filter(entry -> entry.getValue() == max)
                    .map(entry -> entry.getKey())
                    .collect(Collectors.toList());
            if (possibleWinners.size() == 1 || (possibleWinners.size() == 2 && possibleWinners.get(0).getSchoolBoard().getTowerType().equals(possibleWinners.get(1).getSchoolBoard().getTowerType()))) {
                winners.addAll(possibleWinners);
                end = true;
            } else {
                //TODO: Tie
            }
        }
        return end;
    }
}
