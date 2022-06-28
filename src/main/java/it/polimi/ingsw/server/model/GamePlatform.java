package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.board.Bag;
import it.polimi.ingsw.server.model.board.GameBoard;
import it.polimi.ingsw.server.model.board.SpecialCharacter;
import it.polimi.ingsw.server.model.player.Assistant;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.utilities.*;
import it.polimi.ingsw.utilities.exceptions.AlreadyExistingPlayerException;
import it.polimi.ingsw.utilities.exceptions.FullGameException;
import it.polimi.ingsw.utilities.exceptions.RoundConcluded;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Core of the entire game structure.
 *
 * @author Riccardo Motta
 */
public class GamePlatform {

    private final boolean expert;
    private final GameBoard gameBoard;
    private final List<Player> clockwiseOrder;
    private final List<Player> turnOrder;
    private final Map<String, Player> players;
    private final int playersNumber;
    private String currentPlayer;
    private String roundWinner;

    /**
     * Class constructor.
     *
     * @param playersNumber Number of players that the game requires.
     * @param expert        Indicates whether the game has to be played in expert mode or not.
     */
    public GamePlatform(int playersNumber, boolean expert) {
        this.expert = expert;
        this.gameBoard = new GameBoard(playersNumber, expert);
        this.clockwiseOrder = new ArrayList<>();
        this.turnOrder = new ArrayList<>();
        this.players = new HashMap<>();
        this.playersNumber = playersNumber;
        this.currentPlayer = "";
        this.roundWinner = "";

        Log.info("*** New GamePlatform successfully created.");
    }

    /**
     * Class constructor used to restore the game.
     *
     * @param expert         Indicates whether the game has to be played in expert mode or not.
     * @param gameBoard      Game's board.
     * @param clockwiseOrder List of players in clockwise order.
     * @param turnOrder      List of players ordered by turn.
     * @param currentPlayer  Current playing player.
     */
    public GamePlatform(boolean expert, GameBoard gameBoard, List<Player> clockwiseOrder, List<Player> turnOrder, String currentPlayer) {
        this.expert = expert;
        this.gameBoard = gameBoard;
        this.clockwiseOrder = new ArrayList<>(clockwiseOrder);
        this.turnOrder = new ArrayList<>(turnOrder);
        this.playersNumber = clockwiseOrder.size();
        this.currentPlayer = currentPlayer;
        this.roundWinner = turnOrder.get(0).getName();
        this.players = new HashMap<>();
        this.clockwiseOrder.forEach(player -> this.players.put(player.getName(), player));

        Log.info("*** Saved GamePlatform successfully restored.");
    }

    /**
     * Gets the clockwise order of the players.
     *
     * @return Clockwise order of the players.
     */
    public List<Player> getPlayers() {
        return new ArrayList<>(clockwiseOrder);
    }

    /**
     * Gets the turn order.
     *
     * @return The turn order.
     */
    public List<Player> getTurnOrder() {
        return new ArrayList<>(turnOrder);
    }

    /**
     * Adds a player to the game.
     *
     * @param name Name of the player.
     * @throws AlreadyExistingPlayerException If a player with the same name already exists.
     * @throws FullGameException              If there are already enough players.
     */
    public void addPlayer(String name)
            throws AlreadyExistingPlayerException, FullGameException {
        Player tmp;
        if (players.size() == playersNumber)
            throw new FullGameException("There are already " + playersNumber + "/" + playersNumber + " players.");
        if (players.containsKey(name))
            throw new AlreadyExistingPlayerException("A player with name \"" + name + "\" already exists.");

        Map<HouseColor, Integer> students = new EnumMap<>(HouseColor.class);
        for (HouseColor color : HouseColor.values()) students.put(color, 0);
        int studentsNumber = 7;
        if (this.playersNumber == 3)
            studentsNumber = 9;
        for (int i = 0; i < studentsNumber; i++) {
            HouseColor color = this.getGameBoard().getBag().pop();
            students.put(color, students.get(color) + 1);
        }
        tmp = new Player(name, getWizardType(), getTowersNumber(), getTowerType(), students);
        clockwiseOrder.add(tmp);
        turnOrder.add(tmp);
        players.put(name, tmp);
        if (players.size() == 1) {
            currentPlayer = name;
            roundWinner = name;
        }
    }

    /**
     * Returns a random Wizard which has not yet been used.
     *
     * @return The chosen Wizard.
     */
    private WizardType getWizardType() {
        List<WizardType> wizards = new ArrayList<>();
        for (WizardType wizard : WizardType.values())
            if (clockwiseOrder.stream().noneMatch(player -> player.getWizard().equals(wizard)))
                wizards.add(wizard);
        return wizards.get(ThreadLocalRandom.current().nextInt(wizards.size()));
    }

    /**
     * Returns the number of towers for the player, according to the number of players.
     *
     * @return The number of towers.
     */
    private int getTowersNumber() {
        if (playersNumber == 3)
            return 6;
        else
            return 8;
    }

    /**
     * Returns a random Tower according to game rules.
     *
     * @return The chosen Tower.
     */
    private TowerType getTowerType() {
        List<TowerType> towers = new ArrayList<>();
        if (playersNumber == 3) {
            for (TowerType tower : TowerType.values())
                if (clockwiseOrder.stream().noneMatch(player -> player.getSchoolBoard().getTowerType().equals(tower)))
                    towers.add(tower);
        } else {
            int whiteCount = (int) clockwiseOrder.stream().filter(player -> player.getSchoolBoard().getTowerType().equals(TowerType.WHITE)).count();
            int blackCount = (int) clockwiseOrder.stream().filter(player -> player.getSchoolBoard().getTowerType().equals(TowerType.BLACK)).count();

            for (int index = (playersNumber - whiteCount) / 2; index > 0 && whiteCount < 2; index--)
                towers.add(TowerType.WHITE);
            for (int index = (playersNumber - blackCount) / 2; index > 0 && blackCount < 2; index--)
                towers.add(TowerType.BLACK);
        }
        return towers.get(ThreadLocalRandom.current().nextInt(towers.size()));
    }

    /**
     * Gets the player associated with a specific name.
     *
     * @param name Name of the player.
     * @return Required player.
     */
    public Player getPlayerByName(String name) {
        return players.get(name);
    }

    /**
     * Gets if the game is played in expert mode.
     *
     * @return If the game is played in expert mode.
     */
    public boolean isExpert() {
        return expert;
    }

    /**
     * Gets the current player.
     *
     * @return Current player.
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayer);
    }

    /**
     * Gets a reference to the game board.
     *
     * @return Reference to the game board.
     */
    public GameBoard getGameBoard() {
        return gameBoard;
    }

    /**
     * Moves to the next round.
     *
     * @throws EmptyStackException Thrown when the bag has no more students.
     */
    public void nextRound() throws EmptyStackException {
        Bag bag = gameBoard.getBag();
        gameBoard.flushAssistantsList();
        gameBoard.getClouds().forEach(cloud -> {
            HouseColor houseColor;
            Map<HouseColor, Integer> map = new EnumMap<>(HouseColor.class);
            for (HouseColor color : HouseColor.values())
                map.put(color, 0);
            for (int i = 0; i < (playersNumber == 3 ? 4 : 3); i++) {
                houseColor = bag.pop();
                map.replace(houseColor, map.get(houseColor) + 1);
            }
            cloud.refill(map);
        });
        currentPlayer = roundWinner;
    }

    /**
     * Reorders the turn players according to the played assistants.
     */
    public void updateTurnOrder() {
        Map<Player, Assistant> playedAssistants = gameBoard.getPlayedAssistants();
        getNewOrder(roundWinner, playedAssistants);
        roundWinner = turnOrder.get(0).getName();
        currentPlayer = roundWinner;
    }

    /**
     * Moves to the next player, unless the round is concluded.
     *
     * @throws RoundConcluded when the round is finished.
     */
    public void nextTurn() throws RoundConcluded {
        String player;
        gameBoard.removeEffects();

        Log.debug(players.get(currentPlayer).getName());
        Log.debug(String.valueOf((turnOrder.indexOf(players.get(currentPlayer)) + 1) % playersNumber));
        player = turnOrder.get((turnOrder.indexOf(players.get(currentPlayer)) + 1) % playersNumber).getName();

        if (expert)
            gameBoard.getCharacters().forEach(SpecialCharacter::changedTurn);

        if (player.equals(roundWinner))
            throw new RoundConcluded();

        currentPlayer = player;
    }

    /**
     * Sorts the turn list to get the new order.
     *
     * @param roundWinner      Winner of the previous round.
     * @param playedAssistants Map of players and their played assistants.
     */
    private void getNewOrder(String roundWinner, Map<Player, Assistant> playedAssistants) {
        Player player;
        List<Pair<Player, Assistant>> playedAssistantsOrder = reorderPlayedCards(roundWinner, playedAssistants);

        List<Player> remainingPlayers = new ArrayList<>(clockwiseOrder);
        turnOrder.clear();

        while (!remainingPlayers.isEmpty()) {
            player = playedAssistantsOrder.stream()
                    .filter(pair -> {
                        OptionalInt optional = remainingPlayers.stream()
                                .mapToInt(item -> playedAssistants.get(item).getId())
                                .min();
                        return optional.isPresent() && pair.second().getId() == optional.getAsInt();
                    })
                    .toList()
                    .get(0)
                    .first();
            turnOrder.add(player);
            remainingPlayers.remove(player);
        }
    }

    /**
     * Gets the list of played cards in clockwise order starting from the previous turn winner.
     *
     * @param roundWinner      Index of the previous round winner in clockwisePlayersOrder.
     * @param playedAssistants Map of players and their played assistants.
     * @return A list ordered from the first player who dropped a card, to the last one.
     */
    private List<Pair<Player, Assistant>> reorderPlayedCards(String roundWinner, Map<Player, Assistant> playedAssistants) {
        Player player;
        int roundWinnerIndex = clockwiseOrder.indexOf(players.get(roundWinner));
        List<Pair<Player, Assistant>> playedAssistantsOrder = new ArrayList<>();
        try {
            for (int index = 0; index < playersNumber; index++) {
                player = clockwiseOrder.get((roundWinnerIndex + index) % playersNumber);
                playedAssistantsOrder.add(new Pair<>(player, playedAssistants.get(player)));
            }
        } catch (Exception e) {
            Log.error(e);
            System.exit(1);
        }
        return playedAssistantsOrder;
    }
}
