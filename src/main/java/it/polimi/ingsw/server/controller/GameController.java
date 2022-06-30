package it.polimi.ingsw.server.controller;

import com.google.gson.JsonObject;
import it.polimi.ingsw.server.Server;
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
public class GameController implements Runnable {
    private final int expectedPlayers;
    private final GamePlatform gameModel;
    private final String savePath;
    private final Map<String, User> users;
    private final String id;
    private final Object isFullLock;
    private final Object actionNeededLock;
    private final Server server;
    private int connectedPlayers;
    private int round;
    private Phase phase;
    private GameControllerState subPhase;
    private String activeUser;
    private boolean initialized;
    private boolean ended;

    /**
     * The game controller constructor.
     *
     * @param server          The main instance of the server used for deleting a game.
     * @param id              The id of the game.
     * @param gameModel       The reference to the game model, that represent the game state.
     * @param expectedPlayers The number of expected player.
     * @param savePath        The path to the location where the game will be saved.
     */
    public GameController(Server server, String id, GamePlatform gameModel, int expectedPlayers, String savePath) {
        this.server = server;
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
        this.subPhase = GameControllerState.PLAY_ASSISTANT;
        this.activeUser = null;
        this.initialized = false;
        this.ended = false;

        Log.info("*** New GameController successfully created with : " + id);
    }

    /**
     * Game controller constructor user for restoring the game.
     *
     * @param server          The main instance of the server used for deleting a game.
     * @param id              The id of the game.
     * @param gameModel       The reference to the game model, that represent the game state.
     * @param expectedPlayers The number of expected player.
     * @param round           The number of the current round.
     * @param phase           The current phase of the game.
     * @param subPhase        The current subphase of the game.
     * @param players         The list of players names who were present in the game.
     * @param savePath        The path to the location where the game will be saved.
     * @throws FullGameException              Thrown when the game to which the user is attempting to log in is already full and active.
     * @throws AlreadyExistingPlayerException Thrown when the user is attempting to log into a game that has already got an active player with the same chosen username.
     */
    public GameController(Server server, String id, GamePlatform gameModel, int expectedPlayers, int round, String phase, String subPhase, Set<String> players, String savePath) throws AlreadyExistingPlayerException, FullGameException {
        this.server = server;
        this.connectedPlayers = 0;
        this.expectedPlayers = expectedPlayers;
        this.gameModel = gameModel;
        this.id = id;
        this.phase = Phase.valueOf(phase);
        this.round = round;
        this.savePath = savePath;
        this.users = new HashMap<>();
        for (String player : players)
            addUser(player, null);
        this.connectedPlayers = 0;
        this.isFullLock = new Object();
        this.actionNeededLock = new Object();
        this.subPhase = GameControllerState.valueOf(subPhase);
        this.activeUser = null;
        this.initialized = true;
        this.ended = false;

        Log.info("*** Successfully restored game controller : " + id);
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
     * This method tells if the game is ended.
     *
     * @return The ended attribute.
     */
    public boolean isEnded() {
        return this.ended;
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
    public GameControllerState getSubPhase() {
        return subPhase;
    }

    /**
     * This method is called whenever the gameController needs to change it's state, because of the execution of a certain command.
     *
     * @param state The state to set into the subPhase attribute.
     */
    private void setSubPhase(GameControllerState state) {
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

        if (isFull() && user != null) saveGame();
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
    public void saveGame() {

        JsonObject json = new JsonObject();
        json.addProperty("id", this.id);
        json.addProperty("expert", this.gameModel.isExpert());
        json.addProperty("currentPlayer", this.gameModel.getCurrentPlayer().getName());
        json.add("clockwiseOrder", ObjectsToJson.toJsonArray(this.gameModel.getPlayers(), ObjectsToJson.GET_NAMES));
        json.add("turnOrder", ObjectsToJson.toJsonArray(this.gameModel.getTurnOrder(), ObjectsToJson.GET_NAMES));
        json.addProperty("expectedPlayers", this.expectedPlayers);
        json.addProperty("round", this.getRound());
        json.addProperty("phase", this.getPhase().toString());
        json.addProperty("subPhase", this.getSubPhase().toString());
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
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(this.savePath, this.id + ".json"), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {
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
        while (!ended) {
            while (!this.isFull()) {
                try {
                    synchronized (this.isFullLock) {
                        this.isFullLock.wait();
                    }
                } catch (InterruptedException e) {
                    notifyUsers(MessageCreator.error("GameServerError"));
                    for (User user : this.getUsers()) this.removeUser(user);
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            if (!initialized) {
                this.getGameModel().nextRound();
                initialized = true;
            }
            if (this.getPhase() == Phase.PLANNING) this.planningPhase();
            else this.actionPhase();
        }
        for (User user : this.getUsers()) {
            this.removeUser(user);
        }
        try {
            Files.delete(Paths.get(this.savePath, this.id + ".json"));
            Log.info(String.format("Deleted %s after game end.", this.id));
            server.removeGame(id);
        } catch (Exception e) {
            Log.warning(String.format("The following error occurred while trying to delete %s, please delete it manually", this.id), e);
        }
    }

    /**
     * This method manages the planning phase.
     */
    private void planningPhase() {

        Log.info("Planning phase");

        notifyUsers(MessageCreator.status(this));
        saveGame();

        try {
            for (int index = 0; index < expectedPlayers; index++) {
                Player currentPlayer = gameModel.getCurrentPlayer();
                Log.info("Current player: " + currentPlayer.getName());

                // ENABLING THE CURRENT USER INPUT (taken from the clockwise order).
                User currentUser = getUser(currentPlayer.getName());
                this.activeUser = currentUser.getUsername();
                notifyUsers(MessageCreator.turnEnable(currentUser.getUsername(), true));

                // WAITING FOR ASSISTANT TO BE PLAYED
                Log.debug("Waiting for assistant to be played.");
                while (this.getSubPhase() != GameControllerState.ASSISTANT_PLAYED) {
                    synchronized (this.actionNeededLock) {
                        this.actionNeededLock.wait();
                    }
                    synchronized (this.isFullLock) {
                        if (!this.isFull()) {
                            Log.debug("Game is not full.");
                            return;
                        }
                    }
                }

                // DISABLING THE CURRENT USER'S INPUT.
                this.activeUser = null;
                notifyUsers(MessageCreator.turnEnable(currentUser.getUsername(), false));
                this.setSubPhase(GameControllerState.PLAY_ASSISTANT);
                gameModel.nextTurn();
                notifyUsers(MessageCreator.status(this));
            }
        } catch (RoundConcluded e) {
            Log.info("Every player has chosen an assistant.");
        } catch (InterruptedException ie) {
            notifyUsers(MessageCreator.error("GameServerError"));
            for (User user : this.getUsers()) this.removeUser(user);
            Thread.currentThread().interrupt();
        }

        this.getGameModel().updateTurnOrder();
        this.phase = Phase.ACTION;
        this.setSubPhase(GameControllerState.MOVE_STUDENT_1);
        notifyUsers(MessageCreator.status(this));
        saveGame();
    }

    /**
     * This method manages the action phase.
     */
    private void actionPhase() {
        Log.debug("Action phase");

        //ENABLING THE INPUT OF THE CURRENT USER (taken from the turn list).
        User currentUser = getUser(getGameModel().getCurrentPlayer().getName());
        Log.debug("Current player" + currentUser.getUsername());
        this.activeUser = currentUser.getUsername();

        //WAITING FOR A CLOUD TO BE CHOSEN (refill command)
        Log.debug("Waiting for a cloud to be chosen.");
        while (this.getSubPhase() != GameControllerState.END_TURN) {
            notifyUsers(MessageCreator.turnEnable(currentUser.getUsername(), true));
            Log.debug("Current subphase: " + this.subPhase.toString());
            synchronized (this.actionNeededLock) {
                try {
                    this.actionNeededLock.wait();
                    if (isEnded()) return;
                    saveGame();
                    notifyUsers(MessageCreator.status(this));
                } catch (InterruptedException ie) {
                    notifyUsers(MessageCreator.error("GameServerError"));
                    for (User user : this.getUsers())
                        this.removeUser(user);
                    Thread.currentThread().interrupt();
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
            this.setSubPhase(GameControllerState.MOVE_STUDENT_1);
        } catch (RoundConcluded rc) {
            Log.debug("Round concluded.");
            try {
                this.getGameModel().nextRound();
            } catch (EmptyStackException ese) {
                List<Player> winners;
                ended = true;
                winners = checkForWinners();
                notifyUsers(MessageCreator.win(winners));
            }
            this.round++;
            this.phase = Phase.PLANNING;
            this.setSubPhase(GameControllerState.PLAY_ASSISTANT);
        }
        saveGame();
        notifyUsers(MessageCreator.status(this));
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
            this.setSubPhase(GameControllerState.ASSISTANT_PLAYED);
        } catch (IllegalMoveException | AlreadyPlayedException e) {
            Log.warning(e);
            this.getUsers().remove(this.getUser(player));
        }
        synchronized (this.actionNeededLock) {
            this.actionNeededLock.notifyAll();
        }
        Log.debug("Assistant played");
    }

    /**
     * This method manages the movements of the student in the game.
     *
     * @param command The json with the directions of the movements.
     * @throws IllegalMoveException if there are no students left.
     */
    public void moveStudent(JsonObject command) throws IllegalMoveException {
        try {
            moveStudentFrom(command);
        } catch (NoStudentException e) {
            Log.warning(e);
            throw new IllegalMoveException();
        }
        moveStudentTo(command);
        synchronized (this.actionNeededLock) {
            this.actionNeededLock.notifyAll();
        }
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

                if (!command.get("special").getAsBoolean()) {
                    switch (this.getSubPhase()) {
                        case MOVE_STUDENT_1 -> this.setSubPhase(GameControllerState.MOVE_STUDENT_2);

                        case MOVE_STUDENT_2 -> this.setSubPhase(GameControllerState.MOVE_STUDENT_3);
                        case MOVE_STUDENT_3 -> {
                            if ((this.getExpectedPlayers() == 3)) {
                                this.setSubPhase(GameControllerState.MOVE_STUDENT_4);
                            } else {
                                this.setSubPhase(GameControllerState.MOVE_MOTHER_NATURE);
                            }
                        }
                        case MOVE_STUDENT_4 -> this.setSubPhase(GameControllerState.MOVE_MOTHER_NATURE);
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
                        ((MonkEffect) c.getEffect()).effect(null, gameModel.getGameBoard().getBag().pop());
                        check = true;
                        break;
                    } else if (c.getId() == 7 && c.isActive()) {
                        ((JesterEffect) c.getEffect()).effect(HouseColor.valueOf(command.get("color").getAsString()), null);
                        check = true;
                        break;
                    } else if (c.getId() == 11 && c.isActive()) {
                        ((PrincessEffect) c.getEffect()).effect(HouseColor.valueOf(command.get("color").getAsString()), null);
                        ((PrincessEffect) c.getEffect()).effect(null, gameModel.getGameBoard().getBag().pop());
                        check = true;
                        break;
                    }
                }
                if (!check)
                    throw new IllegalMoveException();
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
                if (this.gameModel.isExpert() && this.gameModel.getPlayerByName(command.get("player").getAsString()).getSchoolBoard().getStudentsNumberOf(HouseColor.valueOf(command.get("color").getAsString())) % 3 == 0) {
                    this.gameModel.getPlayerByName(command.get("player").getAsString()).addCoins();
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
            case "entrance" -> {
                Map<HouseColor, Integer> tmp = new EnumMap<>(HouseColor.class);
                tmp.put(HouseColor.valueOf(command.get("color").getAsString().toUpperCase(Locale.ROOT)), 1);

                this.gameModel.getCurrentPlayer().getSchoolBoard().addToEntrance(tmp);
            }
            default -> throw new IllegalMoveException();
        }
    }

    /**
     * This method manages the movements of mother nature in the game and her consequences.
     *
     * @param idIsland The id of the island where mother nature will be set.
     * @param move     true when mother nature has to be moved, false when it's used for resolving an island.
     * @throws IllegalMoveException if one of the actions can't be performed.
     */
    public void moveMotherNature(int idIsland, boolean move) throws IllegalMoveException {
        Island targetIsland;

        try {
            targetIsland = this.gameModel.getGameBoard().getIslandById(idIsland);
            if (move) {
                this.gameModel.getGameBoard().moveMotherNature(targetIsland, this.gameModel.getGameBoard().getPlayedAssistants().get(this.gameModel.getCurrentPlayer()));
                this.setSubPhase(GameControllerState.CHOOSE_CLOUD);
            }
        } catch (IslandNotFoundException e) {
            Log.warning(e);
            throw new IllegalMoveException();
        }
        if (!targetIsland.isBanned()) {
            motherNatureAction(targetIsland);
        } else {
            targetIsland.removeBan();
            Optional<SpecialCharacter> specialCharacter = getGameModel().getGameBoard().getCharacters().stream().filter(character -> character.getId() == 5).findFirst();
            try {
                if (specialCharacter.isPresent())
                    ((HerbalistEffect) specialCharacter.get().getEffect()).effect(HerbalistEffect.Action.PUT_BACK);
                else throw new IllegalMoveException();
            } catch (NoMoreBansLeftException e) {
                throw new IllegalMoveException();
            }
        }

        synchronized (this.actionNeededLock) {
            this.actionNeededLock.notifyAll();
        }
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

        final int max;
        if (!influence.keySet().isEmpty()) {
            max = Collections.max(influence.values());

            if (max > 0) {
                List<Player> mostInfluential = influence.entrySet().stream().filter(entry -> entry.getValue() == max).map(Map.Entry::getKey).toList();
                if (mostInfluential.size() == 1 || (mostInfluential.size() == 2 && mostInfluential.get(0).getSchoolBoard().getTowerType().equals(mostInfluential.get(1).getSchoolBoard().getTowerType()))) {
                    if (island.getTower() == null) {
                        try {
                            for (Player player : this.gameModel.getPlayers()) {
                                if (player.getSchoolBoard().getTowerType().equals(mostInfluential.get(0).getSchoolBoard().getTowerType())) {
                                    player.getSchoolBoard().removeTowers(island.getSize());
                                }
                            }
                            endGame();
                        } catch (NotEnoughTowersException e1) {
                            endGame();
                        } catch (NegativeException e2) {
                            throw new IllegalMoveException();
                        }

                        this.getGameModel().getGameBoard().setTowerOnIsland(island, mostInfluential.get(0).getSchoolBoard().getTowerType());
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

                                endGame();
                            } catch (NotEnoughTowersException e1) {
                                endGame();
                            } catch (NegativeException e2) {
                                throw new IllegalMoveException();
                            }
                        }
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
        this.setSubPhase(GameControllerState.END_TURN);
        endGame();
        synchronized (this.actionNeededLock) {
            this.actionNeededLock.notifyAll();
        }
    }

    /**
     * This method manages the payment of the special character.
     *
     * @param command The json that contains all the information of the special character that will be paid.
     * @throws IllegalMoveException if one of the actions can't be performed.
     */
    public void paySpecialCharacter(JsonObject command) throws IllegalMoveException {
        boolean characterAlreadyPlayed = false;
        try {
            for (SpecialCharacter c : this.getGameModel().getGameBoard().getCharacters()) {
                if (c.isActive()) {
                    characterAlreadyPlayed = true;
                    break;
                }
            }
        } catch (Exception e) {
            Log.warning(e);
        }
        if (characterAlreadyPlayed)
            throw new IllegalMoveException();

        int character = command.get("character").getAsInt();
        try {
            SpecialCharacter param = null;
            for (SpecialCharacter c : this.getGameModel().getGameBoard().getCharacters())
                if (c.getId() == command.get("character").getAsInt()) {
                    param = c;
                    break;
                }
            if (param == null) throw new IllegalMoveException();
            this.getGameModel().getPlayerByName(command.get("player").getAsString()).paySpecialCharacter(param);
        } catch (Exception e) {
            Log.warning(e);
            throw new IllegalMoveException();
        }

        try {
            switch (character) {
                case 2 -> {
                    this.getGameModel().getGameBoard().setTieWinner(this.getGameModel().getPlayerByName(command.get("player").getAsString()));
                    Arrays.stream(HouseColor.values()).forEach(color -> {
                        if (this.gameModel.getGameBoard().getProfessors().get(color) != null)
                            checkProfessor(String.valueOf(color), this.gameModel.getCurrentPlayer().getName());
                    });
                }
                case 4 ->
                        this.gameModel.getGameBoard().getPlayedAssistants().get(this.gameModel.getPlayerByName(command.get("player").getAsString())).setBonus();
                case 8 ->
                        gameModel.getGameBoard().setInfluenceBonus(this.getGameModel().getPlayerByName(command.get("player").getAsString()));
                default -> {
                }
            }
        } catch (Exception nfe) {
            throw new IllegalMoveException();
        }
        synchronized (this.actionNeededLock) {
            this.actionNeededLock.notifyAll();
        }
    }

    /**
     * This method sets the second Ban.
     *
     * @param island The id of the island
     * @throws IllegalMoveException This exception is thrown whether the player tries to do illegal move.
     */
    public void setBan(int island) throws IllegalMoveException {
        try {
            this.gameModel.getGameBoard().getIslandById(island).setBan();
            for (SpecialCharacter sc : this.gameModel.getGameBoard().getCharacters()) {
                if (sc.getId() == 5) ((HerbalistEffect) sc.getEffect()).effect(HerbalistEffect.Action.TAKE);
            }
        } catch (IslandNotFoundException | NoMoreBansLeftException e) {
            throw new IllegalMoveException();
        }
        synchronized (this.actionNeededLock) {
            this.actionNeededLock.notifyAll();
        }
    }

    /**
     * This method sets thr ignored color.
     *
     * @param color The color which will be ignored.
     */
    public void setIgnoredColor(HouseColor color) {
        gameModel.getGameBoard().setIgnoreColor(color);
        synchronized (this.actionNeededLock) {
            this.actionNeededLock.notifyAll();
        }
    }

    /**
     * Method calls to use 'return' effect.
     *
     * @param color The color that will be returned.
     */
    public void returnStudents(HouseColor color) {
        for (Player p : gameModel.getPlayers()) {
            for (int i = 0; i < 3; i++) {
                try {
                    p.getSchoolBoard().removeFromDiningRoom(color);
                    gameModel.getGameBoard().getBag().push(color);
                } catch (NoStudentException nse) {
                    break;
                }
            }
        }
        for (Player p : gameModel.getPlayers()) this.checkProfessor(color.toString(), p.getName());

        synchronized (this.actionNeededLock) {
            this.actionNeededLock.notifyAll();
        }
    }

    /**
     * This method manages the win condition for the game.
     */
    public void endGame() {
        synchronized (this.users) {
            List<Player> winners = new ArrayList<>();
            boolean assistantsEmpty = true;
            for (int i = 0; i < this.gameModel.getPlayers().get(0).getAssistants().size(); i++)
                if (this.gameModel.getPlayers().get(0).getAssistants().get(i) != null) assistantsEmpty = false;

            if (this.gameModel.getPlayers().stream().anyMatch(player -> player.getSchoolBoard().getTowersNumber() == 0)) {
                ended = true;
                for (Player player : this.gameModel.getPlayers())
                    if (player.getSchoolBoard().getTowersNumber() == 0) winners.add(player);
            } else if ((this.gameModel.getGameBoard().getIslands().size() == 3) || (this.gameModel.getCurrentPlayer().equals(this.gameModel.getTurnOrder().get(this.expectedPlayers - 1)) && (this.gameModel.getGameBoard().getBag().isEmpty() || assistantsEmpty))) {
                ended = true;
                winners = checkForWinners();
            }

            if (ended) notifyUsers(MessageCreator.win(winners));
        }
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
                if (this.gameModel.getGameBoard().getProfessors().get(color) != null) {
                    player = this.gameModel.getGameBoard().getProfessors().get(color);
                    numProfessorsPlayers.put(player, numProfessorsPlayers.containsKey(player) ? numProfessorsPlayers.get(player) + 1 : 1);
                }
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
     *
     * @param color  The color of the professor.
     * @param player The name of the player.
     */
    public void checkProfessor(String color, String player) {
        String newProfessorOwner = null;
        if (this.gameModel.getGameBoard().getProfessors().get(HouseColor.valueOf(color)) != null) {
            newProfessorOwner = this.gameModel.getGameBoard().getProfessors().get(HouseColor.valueOf(color)).getName();
        } else if (this.gameModel.getPlayerByName(player).getSchoolBoard().getStudentsNumberOf(HouseColor.valueOf(color)) > 0)
            newProfessorOwner = player;

        int numStudent;
        if (newProfessorOwner != null) {
            numStudent = this.gameModel.getPlayerByName(newProfessorOwner).getSchoolBoard().getStudentsNumberOf(HouseColor.valueOf(color));
            for (Player p : this.gameModel.getPlayers()) {
                if (numStudent < p.getSchoolBoard().getStudentsNumberOf(HouseColor.valueOf(color))) {
                    numStudent = p.getSchoolBoard().getStudentsNumberOf(HouseColor.valueOf(color));
                    newProfessorOwner = p.getName();
                } else if (this.gameModel.getGameBoard().getTieWinner() != null && this.gameModel.getGameBoard().getTieWinner().equals(p) && numStudent == p.getSchoolBoard().getStudentsNumberOf(HouseColor.valueOf(color))) {
                    numStudent = p.getSchoolBoard().getStudentsNumberOf(HouseColor.valueOf(color));
                    newProfessorOwner = p.getName();
                }
            }

            this.gameModel.getGameBoard().setProfessor(HouseColor.valueOf(color), this.gameModel.getPlayerByName(newProfessorOwner));
        }
    }

    /**
     * Sends a message to the users.
     *
     * @param message The JsonObject containing the message.
     */
    public void notifyUsers(JsonObject message) {
        synchronized (this.users) {
            for (User user : this.getUsers()) {
                if (user != null) user.sendMessage(message);
            }
        }
    }

    /**
     * Checks the start condition of the game, if it's full it starts the game.
     */
    public void checkStartCondition() {
        if (this.isFull()) {
            this.notifyUsers(MessageCreator.status(this));
            saveGame();
            Log.debug("Status message sent to users.");
            this.notifyUsers(MessageCreator.gameStart());
            Log.debug("GameStart message sent to users.");
            synchronized (this.isFullLock) {
                this.isFullLock.notifyAll();
            }
        }
    }
}
