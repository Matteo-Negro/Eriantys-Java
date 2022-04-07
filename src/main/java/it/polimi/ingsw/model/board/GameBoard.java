package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.player.Assistant;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.utilities.HouseColor;
import it.polimi.ingsw.model.utilities.exceptions.IllegalMoveException;

import java.util.*;

/**
 * GameBoard Class, main classes, here there are all board elements.
 *
 * @author Matteo Negro
 */
public class GameBoard {
    //TODO: fix Player influencesBonus
    private final Bag bag;
    private final int numPlayer;
    private final List<Cloud> clouds;
    private Island motherNatureIsland;
    private HouseColor ignoreColor;
    private List<Island> islands;
    private List<SpecialCharacter> characters;
    private Map<HouseColor, Player> professors;
    private Map<Player, Assistant> playedAssistants;

    /**
     * GameBoard Constructor, this constructor initializes all the elements in the board.
     *
     * @param numPlayer This parameter contains the number of the player.
     * @param isExp     This parameter is set true whether the game is in expert mode.
     */
    public GameBoard(int numPlayer, boolean isExp) {
        List<HouseColor> temp;
        List<Integer> randomVector = new Vector<>();

        this.bag = new Bag();
        this.numPlayer = numPlayer;
        this.ignoreColor = null;
        this.playedAssistants = new HashMap<>();
        this.islands = new ArrayList<>();
        this.clouds = new ArrayList<>();
        this.characters = new ArrayList<>();
        this.professors = new HashMap<>();
        temp = this.bag.boardSetUp();
        for (int i = 0; i < 12; i++) {
            if (i == 0 || i == 5) this.islands.add(new Island(null, i));
            else this.islands.add(new Island(temp.get(i < 5 ? i : i - 1), i));
        }
        if (isExp) {
            for (int i = 0; i < 12; i++) randomVector.add(i);
            Collections.shuffle(randomVector);
            for (int i = 0; i < 3; i++) {
                this.characters.add(new SpecialCharacter(i));
            }
        }
        Arrays.stream(HouseColor.values()).forEach(color -> this.professors.put(color, null));
        this.initializeClouds();
        this.motherNatureIsland = this.islands.get(0);
    }

    /**
     * This method exposes the rep of the bag with the students.
     *
     * @return The Bag of students that hasn't been already played.
     */
    public Bag getBag() {
        return this.bag;
    }

    /**
     * This method returns the number of player in the game.
     *
     * @return The number of player.
     */
    public int getNumPlayer() {
        return this.numPlayer;
    }

    /**
     * This method returns a copy of clouds.
     *
     * @return The data structure that contains all the clouds.
     */
    public List<Cloud> getClouds() {
        return new ArrayList<>(this.clouds);
    }

    /**
     * This method return the island where mother nature is placed.
     *
     * @return The island where mother nature is on.
     */
    public Island getMotherNatureIsland() {
        return this.motherNatureIsland;
    }

    /**
     * This method moves mother nature from her current position to targetIsland whether is permitted.
     *
     * @param targetIsland    The island where the player would like to move mother nature.
     * @param playedAssistant
     * @throws IllegalMoveException Player would like moves MotherNature over assistant card limit.
     */
    public void moveMotherNature(Island targetIsland, Assistant playedAssistant) throws IllegalMoveException {
        int maxDistance;

        maxDistance = playedAssistant.getMaxDistance();
        if (targetIsland.getId() - this.motherNatureIsland.getId() > maxDistance)
            throw new IllegalMoveException("Player would like moves MotherNature over assistant card limit.");
        this.motherNatureIsland = this.islands.get(targetIsland.getId());
    }

    /**
     * This method returns the color that will be ignored in the evaluations of influence.
     *
     * @return The ignored color.
     */
    public HouseColor getIgnoreColor() {
        return ignoreColor;
    }

    /**
     * This method set the color that will be ignored in the evaluations of influence.
     *
     * @param ignoredColor The color that will be ignored.
     */
    public void setIgnoreColor(HouseColor ignoredColor) {
        this.ignoreColor = ignoredColor;
    }

    /**
     * This method resets all the effects on the board.
     */
    public void removeEffects() {
        this.ignoreColor = null;
    }

    /**
     * This method returns a copy of islands.
     *
     * @return The data structure that contains all the islands.
     */
    public List<Island> getIslands() {
        return new ArrayList<>(this.islands);
    }

    /**
     * This method returns a HashMap of the player with influence on the target island.
     *
     * @param targetIsland The island where influence is evaluated.
     * @return The HashMap where the key is the player and the value the influence on the island.
     */
    public Map<Player, Integer> getInfluence(Island targetIsland) {
        Map<Player, Integer> result = new HashMap<>();
        this.professors.keySet().stream().forEach(professorColor -> {
            if (!professorColor.equals(ignoreColor)) {
                if (!result.containsKey(this.professors.get(professorColor))) {
                    result.put(this.professors.get(professorColor), 0);
                }
                result.put(this.professors.get(professorColor), result.get(professorColor) + targetIsland.getStudents().get(professorColor));
            }
        });

        return result;
    }

    /**
     * This method returns a copy of three characters.
     *
     * @return The data structure three characters.
     */
    public List<SpecialCharacter> getCharacters() {
        return new ArrayList<>(characters);
    }

    /**
     * This method returns a copy of the professors.
     *
     * @return The data structure the professors.
     */
    public Map<HouseColor, Player> getProfessors() {
        return new HashMap<>(this.professors);
    }

    /**
     * The method give access to the data structure professors: the key is the HouseColor, the value is the Player who has the professor.
     *
     * @param color  The house color of the professor.
     * @param player The player who takes the professor.
     */
    public void setProfessors(HouseColor color, Player player) {
        this.professors.put(color, player);
    }

    /**
     * This method adds assistants to the data structure in the class GameBoard.
     *
     * @param currentPlayer   The current player that wolud like to play the card.
     * @param playedAssistant The card that the player would like to play.
     * @throws IllegalMoveException The played card is already played from another player.
     */
    //TODO: throws exception is permitted when it is the last choice
    public void addPlayedAssistant(Player currentPlayer, Assistant playedAssistant) throws IllegalMoveException {
        if (!this.playedAssistants.keySet().stream().anyMatch(player -> this.playedAssistants.get(player) == playedAssistant)) {
            throw new IllegalMoveException("The played card is already played from another player.");
        }
        this.playedAssistants.put(currentPlayer, playedAssistant);
    }

    /**
     * This method cleans the references to all the assistants played in the round.
     */
    public void flushAssistantsList() {
        this.playedAssistants.clear();
    }

    /**
     * The method returns the card played by player.
     *
     * @param player The player who plays the card.
     * @return The card played by the player.
     */
    public Assistant getAssistant(Player player) {
        return playedAssistants.get(player);
    }

    /**
     * This method shows the played assistants.
     *
     * @return The copy of the data structure playeAssistants.
     */
    public Map<Player, Assistant> getPlayedAssistants() {
        return new HashMap<>(this.playedAssistants);
    }

    /**
     * This method initializes the clouds, it is used in the constructor.
     */
    private void initializeClouds() {
        for (int i = 0; i < numPlayer; i++) {
            this.clouds.add(new Cloud(i));
        }
    }
}
