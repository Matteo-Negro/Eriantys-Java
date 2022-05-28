package it.polimi.ingsw.server.controller;

import com.google.gson.JsonObject;
import it.polimi.ingsw.server.model.GamePlatform;
import it.polimi.ingsw.server.model.board.Island;
import it.polimi.ingsw.server.model.board.SpecialCharacter;
import it.polimi.ingsw.server.model.board.effects.HerbalistEffect;
import it.polimi.ingsw.server.model.board.effects.JesterEffect;
import it.polimi.ingsw.server.model.board.effects.MonkEffect;
import it.polimi.ingsw.server.model.board.effects.PrincessEffect;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.utilities.*;
import it.polimi.ingsw.utilities.exceptions.*;
import it.polimi.ingsw.utilities.parsers.ObjectsToJson;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

/**
 * GameController
 *
 * @author Matteo Negro
 * @author Riccardo Motta
 * @author Riccardo Milici
 */
public class GameController extends Thread {
    private final int expectedPlayers;
    private final GamePlatform gameModel;
    private final String savePath;
    private final Map<String, User> users;
    private final String id;
    private final Object isFullLock;
    private final Object actionNeededLock;
    private int connectedPlayers;
    private int round;
    private Phase phase;
    private GameControllerStates subPhase;
    private boolean movementEffectActive;
    private String activeUser;

    /**
     * The game controller constructor.
     *
     * @param gameModel       The reference to the game model, that represent the game state.
     * @param expectedPlayers The number of expected player.
     * @param savePath        The path to the location where the game will be saved.
     */
    public GameController(String id, GamePlatform gameModel, int expectedPlayers, String savePath) {
        this.connectedPlayers = 0;
        this.expectedPlayers = expectedPlayers;
        this.gameModel = gameModel;
        this.id = id;
        this.phase = Phase.PLANNING;
        this.round = 0;
        this.savePath = savePath;
        this.users = new HashMap<>();
        this.isFullLock = new Object();
        this.actionNeededLock = new Object();
        this.subPhase = GameControllerStates.PLAY_ASSISTANT;
        this.movementEffectActive = false;
        this.activeUser = null;

        Log.info("*** New GameController successfully created with : " + id);
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
    public Phase getPhase() {
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
    public List<User> getUsers() {
        synchronized (this.users) {
            return new ArrayList<>(this.users.values());
        }
    }

    /**
     * This method returns the user associated to the name in the parameter.
     *
     * @param name The username associated with the searched user.
     * @return The User instance searched if present.
     */
    public User getUser(String name) {
        synchronized (this.users) {
            return this.users.get(name);
        }
    }

    /**
     * This method returns a set of usernames of the online player.
     *
     * @return The set of usernames.
     */
    public List<String> getUsernames() {
        synchronized (this.users) {
            return new ArrayList<>(this.users.keySet());
        }
    }

    /**
     * This method returns the gameController's current state (indicated by the subPhase).
     *
     * @return The subPhase attribute.
     */
    public GameControllerStates getSubPhase() {
        return subPhase;
    }

    /**
     * This method is called whenever the gameController needs to change it's state, because of the execution of a certain command.
     *
     * @param state The state to set into the subPhase attribute.
     */
    private void setSubPhase(GameControllerStates state) {
        this.subPhase = state;
    }

    /**
     * This method returns the user having the communication token.
     *
     * @return The activeUser attribute.
     */
    public String getActiveUser() {
        return this.activeUser;
    }

    /**
     * This method is used in order to check if a special character, with a movement effect, has been paid and is now active during this turn.
     *
     * @return The boolean value stored into the movementEffectActive attribute.
     */
    public boolean isMovementEffectActive() {
        return this.movementEffectActive;
    }

    /**
     * This method adds user to the data structures that contains the online users.
     *
     * @param name The name of the user that will be added.
     * @param user The User that will be added.
     * @throws FullGameException              Thrown when the game to which the user is attempting to log in is already full and active.
     * @throws AlreadyExistingPlayerException Thrown when the user is attempting to log into a game that has already got an active player with the same chosen username.
     */
    public void addUser(String name, User user) throws FullGameException, AlreadyExistingPlayerException {
        if (isFull()) throw new FullGameException();

        synchronized (this.users) {
            if (this.users.get(name) != null || this.users.keySet().size() == this.expectedPlayers && !this.users.containsKey(name))
                throw new AlreadyExistingPlayerException();

            this.users.put(name, user);
            this.connectedPlayers++;

            if (this.gameModel.getPlayers().stream().noneMatch(player -> player.getName().equals(name)))
                this.gameModel.addPlayer(name);
        }
    }

    /**
     * This method removes user from the data structures that contains the online users.
     *
     * @param user The that will be removed.
     */
    public void removeUser(User user) {
        synchronized (this.users) {
            if (user.getUsername() == null) {
                return;
            }
            this.users.replace(user.getUsername(), null);
            this.connectedPlayers--;
        }
        this.notifyUsers(MessageCreator.error("UserDisconnected"));

        synchronized (this.actionNeededLock) {
            this.actionNeededLock.notifyAll();
        }
    }

    /**
     * This method returns whether the game is full.
     *
     * @return True whether the game is full.
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

        new Thread(() -> writeFile(json)).start();
    }

    /**
     * Writes the json containing the state into a file.
     *
     * @param json The json to write.
     */
    private void writeFile(JsonObject json) {
        try (BufferedWriter writer = Files.newBufferedWriter(
                Paths.get(this.savePath, this.id + ".json"),
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE)) {
            writer.write(json.toString());
        } catch (IOException e) {
            Log.warning(e.getMessage());
        }
    }

    /**
     * This method is called whenever the game if full (players are all connected); it gives the input permission token to the current player, this decision is based on the turn order imposed by the model.
     */
    @Override
    public void run() {
        Log.debug("Game controller running");
        while (true) {
            while (!this.isFull()) {
                try {
                    synchronized (this.isFullLock) {
                        this.isFullLock.wait();
                    }
                } catch (InterruptedException e) {
                    notifyUsers(MessageCreator.error("GameServerError"));
                    for (User user : this.getUsers()) this.removeUser(user);
                    return;
                }
            }
            switch (this.getPhase()) {
                case PLANNING -> this.planningPhase();
                case ACTION -> this.actionPhase();
            }
        }
    }

    /**
     * This method manages the planning phase.
     */
    private void planningPhase() {
        Log.debug("Planning phase");
        Player roundWinner = this.getGameModel().getRoundWinner();
        int indexOfRoundWinner = this.getGameModel().getPlayers().indexOf(roundWinner);

        for (int i = 0; i < this.getExpectedPlayers(); i++) {
            if (i == 0) this.getGameModel().nextRound();

            Player currentPlayer = this.getGameModel().getPlayers().get((indexOfRoundWinner + i) % this.getExpectedPlayers());
            Log.debug("Current player" + currentPlayer.getName());

            //ENABLING THE CURRENT USER INPUT (taken from the clockwise order).
            User currentUser = getUser(currentPlayer.getName());
            this.activeUser = currentUser.getUsername();
            notifyUsers(MessageCreator.turnEnable(currentUser.getUsername(), true));

            //WAITING FOR ASSISTANT TO BE PLAYED
            Log.debug("Waiting for assistant to be played.");
            while (this.getSubPhase() != GameControllerStates.ASSISTANT_PLAYED) {
                synchronized (this.actionNeededLock) {
                    try {
                        this.actionNeededLock.wait();

                    } catch (InterruptedException ie) {
                        notifyUsers(MessageCreator.error("GameServerError"));
                        for (User user : this.getUsers()) this.removeUser(user);
                        return;
                    }
                }
                synchronized (this.isFullLock) {
                    if (!this.isFull()) {
                        Log.debug("Game is not full.");
                        return;
                    }

                }
            }

            //DISABLING THE CURRENT USER'S INPUT.
            this.activeUser = null;
            notifyUsers(MessageCreator.turnEnable(currentUser.getUsername(), false));
            this.setSubPhase(GameControllerStates.PLAY_ASSISTANT);
            notifyUsers(MessageCreator.status(this));
        }
        this.getGameModel().updateTurnOrder();
        this.phase = Phase.ACTION;
        this.setSubPhase(GameControllerStates.MOVE_STUDENT_1);
        notifyUsers(MessageCreator.status(this));
    }

    /**
     * This method manages the action phase.
     */
    private void actionPhase() {
        Log.debug("Action phase");
        for (Player currentPlayer : this.getGameModel().getTurnOrder()) {
            //ENABLING THE INPUT OF THE CURRENT USER (taken from the turn list).
            User currentUser = getUser(currentPlayer.getName());
            Log.debug("Current player" + currentPlayer.getName());
            this.activeUser = currentUser.getUsername();

            //WAITING FOR A CLOUD TO BE CHOSEN (refill command)
            Log.debug("Waiting for a cloud to be chosen.");
            while (this.getSubPhase() != GameControllerStates.END_TURN) {
                notifyUsers(MessageCreator.turnEnable(currentUser.getUsername(), true));
                Log.debug("Current subphase " + this.subPhase.toString());
                synchronized (this.actionNeededLock) {
                    try {
                        this.actionNeededLock.wait();
                        notifyUsers(MessageCreator.status(this));
                    } catch (InterruptedException ie) {
                        notifyUsers(MessageCreator.error("GameServerError"));
                        for (User user : this.getUsers()) this.removeUser(user);
                        return;
                    }
                }
                synchronized (this.isFullLock) {
                    if (!this.isFull()) return;
                }
            }

            //DISABLING THE INPUT OF THE CURRENT USER.
            this.activeUser = null;
            notifyUsers(MessageCreator.turnEnable(currentUser.getUsername(), false));

            try {
                this.getGameModel().nextTurn();
                this.setSubPhase(GameControllerStates.MOVE_STUDENT_1);
            } catch (RoundConcluded rc) {
                this.round++;
                this.phase = Phase.PLANNING;
                this.setSubPhase(GameControllerStates.PLAY_ASSISTANT);
            }

            notifyUsers(MessageCreator.status(this));

            if (endGame()) {
                for (User user : this.getUsers()) {
                    this.removeUser(user);
                }
                return;
            }
        }
    }

    /**
     * This method updates the played assistant in game model.
     *
     * @param player    The name of the player that played the card.
     * @param assistant The id of the card that tha player added.
     */
    public void playAssistantCard(String player, int assistant) {
        try {
            this.gameModel.getGameBoard().addPlayedAssistant(this.gameModel.getPlayerByName(player), this.gameModel.getPlayerByName(player).playAssistant(assistant));
            this.setSubPhase(GameControllerStates.ASSISTANT_PLAYED);
        } catch (IllegalMoveException | AlreadyPlayedException e) {
            Log.warning(e);
            this.getUsers().remove(this.getUser(player));
        }
        this.saveGame();
        synchronized (this.actionNeededLock) {
            this.actionNeededLock.notifyAll();
        }
        Log.debug("Assistant played");
    }

    /**
     * This method manages the movements of the student in the game.
     *
     * @param command The json with the directions of the movements.
     */
    public void moveStudent(JsonObject command) throws IllegalMoveException {
        try {
            Log.debug("moveStudentFrom");
            moveStudentFrom(command);
        } catch (NoStudentException e) {
            Log.warning(e);
            throw new IllegalMoveException();
        }
        Log.debug("moveStudentTo");
        moveStudentTo(command);
        synchronized (this.actionNeededLock) {
            this.actionNeededLock.notifyAll();
        }
        Log.debug("Student moved.");
        this.saveGame();
    }

    /**
     * This method is a helper for moveStudent method.
     *
     * @param command The json with the directions of the movements.
     * @throws NoStudentException   This exception is thrown whether there aren't students.
     * @throws IllegalMoveException This exception is thrown whether the player tries to do illegal move.
     */
    private void moveStudentFrom(JsonObject command) throws NoStudentException, IllegalMoveException {
        switch (command.get("from").getAsString()) {
            case "entrance" -> {
                this.gameModel.getPlayerByName(command.get("player").getAsString()).getSchoolBoard().removeFromEntrance(HouseColor.valueOf(command.get("color").getAsString()));


                if (!isMovementEffectActive()) {
                    switch (this.getSubPhase()) {
                        case MOVE_STUDENT_1 -> this.setSubPhase(GameControllerStates.MOVE_STUDENT_2);

                        case MOVE_STUDENT_2 -> this.setSubPhase(GameControllerStates.MOVE_STUDENT_3);
                        case MOVE_STUDENT_3 -> {
                            if ((this.getExpectedPlayers() == 3)) {
                                this.setSubPhase(GameControllerStates.MOVE_STUDENT_4);
                            } else {
                                this.setSubPhase(GameControllerStates.MOVE_MOTHER_NATURE);
                            }
                        }
                        case MOVE_STUDENT_4 -> this.setSubPhase(GameControllerStates.MOVE_MOTHER_NATURE);
                        default -> throw new IllegalMoveException();
                    }
                }
            }
            case "dining-room" -> {
                this.gameModel.getPlayerByName(command.get("player").getAsString()).getSchoolBoard().removeFromDiningRoom(HouseColor.valueOf(command.get("color").getAsString()));
                checkProfessor(command.get("color").getAsString(), command.get("player").getAsString());
            }
            case "card" -> {
                boolean check = false;
                for (SpecialCharacter c : this.gameModel.getGameBoard().getCharacters()) {
                    if (c.getId() == 1 && c.isActive()) {
                        ((MonkEffect) c.getEffect()).effect(HouseColor.valueOf(command.get("color").getAsString()), null);
                        check = true;
                        break;
                    } else if (c.getId() == 7 && c.isActive()) {
                        ((JesterEffect) c.getEffect()).effect(HouseColor.valueOf(command.get("color").getAsString()), null);
                        check = true;
                        break;
                    } else if (c.getId() == 11 && c.isActive()) {
                        ((PrincessEffect) c.getEffect()).effect(HouseColor.valueOf(command.get("color").getAsString()), null);
                        check = true;
                        break;
                    }
                }
                if (!check) {
                    throw new IllegalMoveException();
                }
            }
            default -> throw new IllegalMoveException();
        }
    }

    /**
     * This method is a helper for moveStudent method.
     *
     * @param command The json with the directions of the movements.
     * @throws IllegalMoveException This exception is thrown whether the player tries to do illegal move.
     */
    private void moveStudentTo(JsonObject command) throws IllegalMoveException {
        switch (command.get("to").getAsString()) {
            case "dining-room" -> {
                this.gameModel.getPlayerByName(command.get("player").getAsString()).getSchoolBoard().addToDiningRoom(HouseColor.valueOf(command.get("color").getAsString()));

                checkProfessor(command.get("color").getAsString(), command.get("player").getAsString());
                if (this.gameModel.isExpert()) {
                    if (this.gameModel.getPlayerByName(command.get("player").getAsString()).getSchoolBoard().getStudentsNumberOf(HouseColor.valueOf(command.get("color").getAsString())) % 3 == 0) {
                        this.gameModel.getPlayerByName(command.get("player").getAsString()).addCoins();
                    }
                }
            }
            case "bag" ->
                    this.gameModel.getGameBoard().getBag().push(HouseColor.valueOf(command.get("color").getAsString()));
            case "island" -> {
                try {
                    this.gameModel.getGameBoard().getIslandById(command.get("toId").getAsInt()).addStudent(HouseColor.valueOf(command.get("color").getAsString()));
                } catch (IslandNotFoundException e) {
                    throw new IllegalMoveException();
                }
            }
            case "card" -> {
                boolean check = false;
                for (SpecialCharacter c : this.gameModel.getGameBoard().getCharacters()) {
                    if (c.getId() == 1 && c.isActive()) {
                        ((MonkEffect) c.getEffect()).effect(null, HouseColor.valueOf(command.get("color").getAsString()));
                        check = true;
                        break;
                    } else if (c.getId() == 7 && c.isActive()) {
                        ((JesterEffect) c.getEffect()).effect(null, HouseColor.valueOf(command.get("color").getAsString()));
                        check = true;
                        break;
                    } else if (c.getId() == 11 && c.isActive()) {
                        ((PrincessEffect) c.getEffect()).effect(null, HouseColor.valueOf(command.get("color").getAsString()));
                        check = true;
                        break;
                    }
                }
                if (!check) throw new IllegalMoveException();

            }
            default -> throw new IllegalMoveException();
        }
    }

    /**
     * This method manages the movements of mother nature in the game and her consequences.
     *
     * @param idIsland The id of the island where mother nature will be set.
     */
    public void moveMotherNature(int idIsland) throws IllegalMoveException {
        Island targetIsland;
        try {
            targetIsland = this.gameModel.getGameBoard().getIslandById(idIsland);
            this.gameModel.getGameBoard().moveMotherNature(targetIsland, this.gameModel.getGameBoard().getPlayedAssistants().get(this.gameModel.getCurrentPlayer()));
            this.setSubPhase(GameControllerStates.CHOOSE_CLOUD);
        } catch (IslandNotFoundException e) {
            throw new IllegalMoveException();
        }
        if (!targetIsland.isBanned()) {
            motherNatureAction(targetIsland);
        } else targetIsland.removeBan();

        this.saveGame();
    }

    /**
     * This method is a helper for moveMotherNature method.
     *
     * @param island The id where the mother nature action will be activated.
     * @throws IllegalMoveException This exception is thrown whether the player tries to do an illegal move.
     */
    public void motherNatureAction(Island island) throws IllegalMoveException {
        Map<Player, Integer> influence;

        influence = this.gameModel.getGameBoard().getInfluence(island);
        int max = Collections.max(influence.values());
        List<Player> mostInfluential = influence.entrySet().stream().filter(entry -> entry.getValue() == max).map(Map.Entry::getKey).toList();
        if (mostInfluential.size() == 1 || (mostInfluential.size() == 2 && mostInfluential.get(0).getSchoolBoard().getTowerType().equals(mostInfluential.get(1).getSchoolBoard().getTowerType()))) {
            if (island.getTower() == null) {
                this.getGameModel().getGameBoard().setTowerOnIsland(island, mostInfluential.get(0).getSchoolBoard().getTowerType());
                try {
                    for (Player player : this.gameModel.getPlayers()) {
                        if (player.getSchoolBoard().getTowerType().equals(mostInfluential.get(0).getSchoolBoard().getTowerType())) {
                            player.getSchoolBoard().removeTowers(island.getSize());
                        }
                    }
                } catch (NotEnoughTowersException e1) {
                    endGame();
                } catch (NegativeException e2) {
                    throw new IllegalMoveException();
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
                        this.getGameModel().getGameBoard().setTowerOnIsland(island, mostInfluential.get(0).getSchoolBoard().getTowerType());
                    } catch (NotEnoughTowersException e1) {
                        endGame();
                    } catch (NegativeException e2) {
                        throw new IllegalMoveException();
                    }
                }
            }
        }
    }

    /**
     * This method manages the choosing of the cloud.
     *
     * @param command The json with the information about the choosing of the cloud.
     */
    public void chooseCloud(JsonObject command) {
        this.gameModel.getPlayerByName(command.get("player").getAsString()).getSchoolBoard().addToEntrance(this.gameModel.getGameBoard().getClouds().get(command.get("cloud").getAsInt()).flush());
        this.setSubPhase(GameControllerStates.END_TURN);
        synchronized (this.actionNeededLock) {
            this.actionNeededLock.notifyAll();
        }
        this.saveGame();
    }

    /**
     * This method manages the payment of the special character.
     *
     * @param command The json that contains all the information of the special character that will be paid.
     */
    public void paySpecialCharacter(JsonObject command) throws IllegalMoveException {

        boolean characterAlreadyPaid = false;
        for (SpecialCharacter c : this.getGameModel().getGameBoard().getCharacters()) {
            if (c.isActive()) {
                characterAlreadyPaid = true;
                break;
            }
        }
        if (characterAlreadyPaid) throw new IllegalMoveException();

        int character = command.get("character").getAsInt();
        this.gameModel.getGameBoard().getCharacters().get(character).activateEffect();
        try {
            this.getGameModel().getPlayerByName(command.get("player").getAsString()).paySpecialCharacter(this.getGameModel().getGameBoard().getCharacters().get(character));
        } catch (NotEnoughCoinsException | NullPointerException ne) {
            throw new IllegalMoveException();
        }

        try {
            switch (character) {
                case 1, 7, 10, 11 -> this.movementEffectActive = true;
                case 2 ->
                        this.getGameModel().getGameBoard().setTieWinner(this.getGameModel().getPlayerByName(command.get("player").getAsString()));
                case 3 ->
                        this.getGameModel().getGameBoard().getInfluence(this.getGameModel().getGameBoard().getIslandById(command.get("island").getAsInt()));
                case 4 ->
                        this.getGameModel().getGameBoard().getAssistant(this.getGameModel().getPlayerByName(command.get("player").getAsString())).setBonus();
                case 5 -> {
                    this.getGameModel().getGameBoard().getIslandById(command.get("island").getAsInt()).setBan();
                    ((HerbalistEffect) this.getGameModel().getGameBoard().getCharacters().get(character).getEffect()).effect("take");
                }
                case 6 -> {
                }//Automatically managed by model.
                case 8 ->
                        this.getGameModel().getGameBoard().setInfluenceBonus(this.getGameModel().getPlayerByName(command.get("player").getAsString()));
                case 9 ->
                        this.getGameModel().getGameBoard().setIgnoreColor(HouseColor.valueOf(command.get("ignoreColor").getAsString()));
                case 12 -> {
                    for (Player p : this.getGameModel().getPlayers()) {
                        for (int i = 0; i < 3; i++) {
                            try {
                                p.getSchoolBoard().removeFromDiningRoom(HouseColor.valueOf(command.get("color").getAsString()));
                            } catch (NoStudentException nse) {
                                for (HouseColor color : HouseColor.values()) {
                                    p.getSchoolBoard().getDiningRoom().replace(color, 0);
                                }
                            }
                        }
                        for (HouseColor color : HouseColor.values()) {
                            this.checkProfessor(color.toString(), p.getName());
                        }
                    }
                }
            }
        } catch (IslandNotFoundException nfe) {
            throw new IllegalMoveException();
        }
        this.saveGame();
    }

    /**
     * This method sets the value Ban.
     *
     * @param island The id of the island
     * @throws IllegalMoveException This exception is thrown whether the player tries to do illegal move.
     */
    public void setBan(int island) throws IllegalMoveException {
        try {
            this.gameModel.getGameBoard().getIslandById(island).setBan();
        } catch (IslandNotFoundException e) {
            throw new IllegalMoveException();
        }
    }

    /**
     * This method manages the win condition for the game.
     *
     * @return A boolean value, true whether the game will have to end.
     */
    public boolean endGame() {
        boolean end = false;
        List<Player> winners = new ArrayList<>();
        if (this.gameModel.getPlayers().stream().anyMatch(player -> player.getSchoolBoard().getTowersNumber() == 0)) {
            end = true;
            for (Player player : this.gameModel.getPlayers()) {
                if (player.getSchoolBoard().getTowersNumber() == 0) winners.add(player);
            }
        } else if (this.gameModel.getGameBoard().getIslands().size() == 3) {
            end = true;
            winners = checkForWinners();
        } else if ((this.gameModel.getCurrentPlayer().equals(this.gameModel.getTurnOrder().get(this.expectedPlayers - 1)))) {
            if (this.gameModel.getGameBoard().getBag().isEmpty() || this.gameModel.getPlayers().get(0).getAssistants().isEmpty()) {
                end = true;
                winners = checkForWinners();
            }
        }
        if (end) {
            notifyUsers(MessageCreator.win(winners));
        }

        return end;
    }

    /**
     * This method is a helper for the endGame method.
     */
    private List<Player> checkForWinners() {
        List<Player> winners = new ArrayList<>();

        Map<Player, Integer> numTowerPlayers = new HashMap<>();
        for (Player player : this.gameModel.getPlayers()) {
            numTowerPlayers.put(player, player.getSchoolBoard().getTowersNumber());
        }
        int min = Collections.min(numTowerPlayers.values());
        List<Player> possibleWinners = numTowerPlayers.entrySet().stream().filter(entry -> entry.getValue() == min).map(Map.Entry::getKey).toList();
        if (possibleWinners.size() == 1 || (possibleWinners.size() == 2 && possibleWinners.get(0).getSchoolBoard().getTowerType().equals(possibleWinners.get(1).getSchoolBoard().getTowerType()))) {
            winners.addAll(possibleWinners);
        } else {
            Map<Player, Integer> numProfessorsPlayers = new HashMap<>();
            Player player;
            for (HouseColor color : HouseColor.values()) {
                player = this.gameModel.getGameBoard().getProfessors().get(color);
                numProfessorsPlayers.put(player, numProfessorsPlayers.containsKey(player) ? numProfessorsPlayers.get(player) + 1 : 1);
            }
            int max = Collections.max(numProfessorsPlayers.values());
            possibleWinners = numProfessorsPlayers.entrySet().stream().filter(entry -> entry.getValue() == max).map(Map.Entry::getKey).toList();
            if (possibleWinners.size() == 1 || (possibleWinners.size() == 2 && possibleWinners.get(0).getSchoolBoard().getTowerType().equals(possibleWinners.get(1).getSchoolBoard().getTowerType()))) {
                winners.addAll(possibleWinners);
            }
        }
        return winners;
    }

    /**
     * This method is a helper for the moveStudent method.
     */
    public void checkProfessor(String color, String player) {
        String newProfessorOwner = player;
        if(this.gameModel.getGameBoard().getProfessors().get(HouseColor.valueOf(color))!=null)
            newProfessorOwner = this.gameModel.getGameBoard().getProfessors().get(HouseColor.valueOf(color)).getName();

        int numStudent;
        numStudent = this.gameModel.getPlayerByName(player).getSchoolBoard().getStudentsNumberOf(HouseColor.valueOf(color));

        for (Player p : this.gameModel.getPlayers()) {
            if (numStudent < p.getSchoolBoard().getStudentsNumberOf(HouseColor.valueOf(color))) {
                numStudent = p.getSchoolBoard().getStudentsNumberOf(HouseColor.valueOf(color));
                newProfessorOwner = p.getName();
            } else if (this.gameModel.getGameBoard().getTieWinner()!=null && this.gameModel.getGameBoard().getTieWinner().equals(p) && numStudent == p.getSchoolBoard().getStudentsNumberOf(HouseColor.valueOf(color))) {
                numStudent = p.getSchoolBoard().getStudentsNumberOf(HouseColor.valueOf(color));
                newProfessorOwner = p.getName();
            }
        }

        if(newProfessorOwner!=null)
            this.gameModel.getGameBoard().setProfessor(HouseColor.valueOf(color), this.gameModel.getPlayerByName(newProfessorOwner));
    }

    public void notifyUsers(JsonObject message) {
        synchronized (this.users) {
            for (User user : this.getUsers()) {
                if (user != null)
                    user.sendMessage(message);
            }
        }
    }

    public void notifyUsersExcept(JsonObject message, User exception) {
        synchronized (this.users) {
            for (User user : this.getUsers()) {
                if (!user.getUsername().equals(exception.getUsername())) user.sendMessage(message);
            }
        }
    }

    public void checkStartCondition() {
        if (this.isFull()) {
            this.notifyUsers(MessageCreator.status(this));
            Log.debug("Status message sent to users.");
            this.notifyUsers(MessageCreator.gameStart());
            Log.debug("GameStart message sent to users.");
            synchronized (this.isFullLock) {
                this.isFullLock.notifyAll();
            }
        }
    }
}
