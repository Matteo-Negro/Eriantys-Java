package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.Bag;
import it.polimi.ingsw.model.board.GameBoard;
import it.polimi.ingsw.model.player.Assistant;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.Pair;
import it.polimi.ingsw.utilities.TowerType;
import it.polimi.ingsw.utilities.WizardType;
import it.polimi.ingsw.utilities.exceptions.AlreadyExistingPlayerException;
import it.polimi.ingsw.utilities.exceptions.FullGameException;
import it.polimi.ingsw.utilities.exceptions.RoundConcluded;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    }

    /**
     * Gets the number of players.
     *
     * @return Number of players.
     */
    public int getPlayersNumber() {
        return playersNumber;
    }

    /**
     * Gets the clockwise order of the players.
     *
     * @return Clockwise order of the players.
     */
    public List<Player> getPlayers() {
        return new ArrayList<>(clockwiseOrder);
    }

    public List<Player> getTurnOrder() {
        return new ArrayList<>(turnOrder);
    }

    /**
     * Adds a player to the game.
     *
     * @param name         Name of the player.
     * @param wizardType   Type of the Wizard.
     * @param towersNumber Number of towers.
     * @param towerType    Color of the tower.
     * @throws AlreadyExistingPlayerException If a player with the same name already exists.
     * @throws FullGameException              If there are already enough players.
     */
    public void addPlayer(String name, WizardType wizardType, int towersNumber, TowerType towerType)
            throws AlreadyExistingPlayerException, FullGameException {
        Player tmp;
        if (players.size() == playersNumber)
            throw new FullGameException("There are already " + playersNumber + "/" + playersNumber + " players.");
        if (players.containsKey(name))
            throw new AlreadyExistingPlayerException("A player with name \"" + name + "\" already exists.");
        tmp = new Player(name, wizardType, towersNumber, towerType);
        clockwiseOrder.add(tmp);
        players.put(name, tmp);
        if (players.size() == 1) {
            currentPlayer = name;
            roundWinner = name;
        }
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
     * Gets the player who played the lowest Assistant.
     *
     * @return The player who played the lowest Assistant.
     */
    public Player getRoundWinner() {
        return players.get(roundWinner);
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
     */
    public void nextRound() {
        Bag bag = gameBoard.getBag();
        gameBoard.flushAssistantsList();
        gameBoard.getClouds().forEach(cloud -> {
            HouseColor houseColor;
            Map<HouseColor, Integer> map = new HashMap<>();
            for (HouseColor color : HouseColor.values())
                map.put(color, 0);
            for (int i = 0; i < (playersNumber == 3 ? 3 : 4); i++) {
                houseColor = bag.pop();
                map.replace(houseColor, map.get(houseColor) + 1);
            }
            cloud.refill(map);
        });
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
     */
    public void nextTurn() throws RoundConcluded {
        String player;
        gameBoard.removeEffects();
        player = turnOrder.get(turnOrder.indexOf(currentPlayer) + 1 % playersNumber).getName();
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
                    .filter(pair -> pair.value().getId() == remainingPlayers.stream()
                            .mapToInt(item -> playedAssistants.get(item).getId())
                            .max()
                            .getAsInt())
                    .toList()
                    .get(0)
                    .key();
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
        int roundWinnerIndex = clockwiseOrder.indexOf(roundWinner);
        List<Pair<Player, Assistant>> playedAssistantsOrder = new ArrayList<>();
        for (int index = 0; index < playersNumber; index++) {
            player = clockwiseOrder.get(roundWinnerIndex + index % playersNumber);
            playedAssistantsOrder.add(new Pair<>(player, playedAssistants.get(player)));
        }
        return playedAssistantsOrder;
    }
}
