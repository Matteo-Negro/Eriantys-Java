package it.polimi.ingsw.server.model.board;

import it.polimi.ingsw.server.model.player.Assistant;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.Log;
import it.polimi.ingsw.utilities.TowerType;
import it.polimi.ingsw.utilities.exceptions.IllegalMoveException;
import it.polimi.ingsw.utilities.exceptions.IslandNotFoundException;

import java.util.*;

/**
 * GameBoard Class, main classes, here there are all board elements.
 *
 * @author Matteo Negro
 */
public class GameBoard {
    private final Bag bag;
    private final List<Cloud> clouds;
    private final List<Island> islands;
    private final Map<HouseColor, Player> professors;
    private final Map<Player, Assistant> playedAssistants;
    private final List<SpecialCharacter> characters;
    private Player influenceBonus;
    private Player tieWinner;
    private Island motherNatureIsland;
    private HouseColor ignoreColor;

    /**
     * GameBoard Constructor, this constructor initializes all the elements in the board.
     *
     * @param playersNumber The number of players.
     * @param isExp         This parameter is set true whether the game is in expert mode.
     */
    public GameBoard(int playersNumber, boolean isExp) {
        List<HouseColor> temp;

        this.bag = new Bag();
        this.ignoreColor = null;
        this.playedAssistants = new HashMap<>();
        this.islands = new ArrayList<>();
        this.clouds = new ArrayList<>();
        this.characters = new ArrayList<>();
        this.professors = new EnumMap<>(HouseColor.class);
        for (HouseColor color : HouseColor.values()) professors.put(color, null);
        this.influenceBonus = null;
        this.tieWinner = null;

        temp = this.bag.boardSetUp();
        for (int i = 0; i < 12; i++) {
            if (i == 0 || i == 6) {
                this.islands.add(new Island(null, i));
            } else this.islands.add(new Island(temp.get(i < 6 ? i - 1 : i - 2), i));
        }

        if (isExp)
            initializeCharacters();
        Arrays.stream(HouseColor.values()).forEach(color -> this.professors.put(color, null));
        this.initializeClouds(playersNumber);
        this.motherNatureIsland = this.islands.get(0);

        Log.info("*** New GameBoard successfully created.");
    }

    /**
     * GameBoard Constructor, this constructor initializes all the elements in the board to the last saved status.
     *
     * @param statusBag              A map that contains the number of students in the bag.
     * @param statusPlayedAssistants A map that contains the played assistants from the saved status.
     * @param statusIslands          A list that contains the islands from the saved status.
     * @param statusClouds           A list that contains the clouds from the saved status.
     * @param statusCharacters       A list that contains the special characters from the saved status.
     * @param statusProfessors       A map that contains the player that posses each professor.
     * @param isExp                  This parameter is set true whether the saved game is in expert mode.
     * @param idMotherNatureIsland   The id of the island where mother nature is in the saved status.
     */
    public GameBoard(Map<HouseColor, Integer> statusBag, Map<Player, Assistant> statusPlayedAssistants, List<Island> statusIslands, List<Cloud> statusClouds, List<SpecialCharacter> statusCharacters, Map<HouseColor, Player> statusProfessors, boolean isExp, int idMotherNatureIsland) {
        this.bag = new Bag(statusBag);
        this.ignoreColor = null;
        this.playedAssistants = new HashMap<>(statusPlayedAssistants);
        this.islands = new ArrayList<>(statusIslands);
        this.clouds = new ArrayList<>(statusClouds);
        this.professors = new EnumMap<>(statusProfessors);
        this.influenceBonus = null;
        this.tieWinner = null;
        this.characters = isExp ? new ArrayList<>(statusCharacters) : List.of();
        try {
            this.motherNatureIsland = this.getIslandById(idMotherNatureIsland);
        } catch (IslandNotFoundException infe) {
            this.motherNatureIsland = this.islands.get(0);
        }

        Log.info("*** Saved GameBoard successfully restored.");
    }

    /**
     * Initializes the special characters.
     */
    private void initializeCharacters() {

        List<Integer> randomVector = new ArrayList<>();

        for (int i = 1; i <= 12; i++) randomVector.add(i);
        Collections.shuffle(randomVector);

        for (int i = 0; i < 3; i++) {

            Map<HouseColor, Integer> students = new EnumMap<>(HouseColor.class);
            for (HouseColor color : HouseColor.values()) students.put(color, 0);
            int studentsNumber;

            studentsNumber = switch (randomVector.get(i)) {
                case 1, 11 -> 4;
                case 7 -> 6;
                default -> 0;
            };

            for (int c = 0; c < studentsNumber; c++) {
                HouseColor color = this.getBag().pop();
                students.replace(color, students.get(color) + 1);
            }
            this.characters.add(new SpecialCharacter(randomVector.get(i), students));
        }

        this.characters.sort(Comparator.comparing((SpecialCharacter::getId)));
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
     * @param playedAssistant The played assistant that shows the max movement that mother nature can do.
     * @throws IllegalMoveException Player would like moves MotherNature over assistant card limit.
     */
    public void moveMotherNature(Island targetIsland, Assistant playedAssistant) throws IllegalMoveException {
        int maxDistance;

        maxDistance = playedAssistant.getMaxDistance();

        int runDistance = 1;
        try {
            Island nextIsland = getIslandById((this.motherNatureIsland.getId() + this.motherNatureIsland.getSize()) % 12);
            while (nextIsland.getId() != targetIsland.getId()) {
                nextIsland = getIslandById((nextIsland.getId() + nextIsland.getSize()) % 12);
                runDistance++;
            }
        } catch (IslandNotFoundException infe) {
            throw new IllegalMoveException();
        }

        if (runDistance > maxDistance)
            throw new IllegalMoveException("Player would like moves MotherNature over assistant card limit.");
        this.motherNatureIsland = targetIsland;
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
        this.influenceBonus = null;
        this.tieWinner = null;
        for (SpecialCharacter c : this.getCharacters())
            c.cleanEffect();
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
     * This method returns the island searching with his id.
     *
     * @param idIsland The island's id.
     * @return The searching island.
     * @throws IslandNotFoundException The exception is throws whether the island that you are searching.
     */
    public Island getIslandById(int idIsland) throws IslandNotFoundException {
        for (Island island : this.islands) {
            if (island.getId() == idIsland) return island;
        }
        throw new IslandNotFoundException("The island that you are searching");
    }

    /**
     * This method returns a HashMap of the player with influence on the target island.
     *
     * @param targetIsland The island where influence is evaluated.
     * @return The HashMap where the first is the player and the second the influence on the island.
     */
    public Map<Player, Integer> getInfluence(Island targetIsland) {
        // Student contribution
        Map<Player, Integer> result = new HashMap<>();
        this.professors.keySet().forEach(professorColor -> {
            if (!professorColor.equals(ignoreColor)) {
                if (!result.containsKey(this.professors.get(professorColor)) && this.professors.get(professorColor) != null)
                    result.put(this.professors.get(professorColor), 0);
                if (this.professors.get(professorColor) != null)
                    result.put(this.professors.get(professorColor), result.get(this.professors.get(professorColor)) + targetIsland.getStudents().get(professorColor) + (influenceBonus != null && this.professors.get(professorColor).equals(this.influenceBonus) ? 2 : 0));
            }
        });

        // Tower contribution
        getInfluenceByTowers(result, targetIsland);

        return result;
    }

    /**
     * Gets the influence points given by towers' contribution.
     *
     * @param partialResult The current influence.
     * @param targetIsland  The island on which the influence is being calculated.
     */
    private void getInfluenceByTowers(Map<Player, Integer> partialResult, Island targetIsland) {
        boolean towersAreIgnored = false;
        for (SpecialCharacter c : this.getCharacters()) {
            if (c.getId() == 6 && c.isActive()) {
                towersAreIgnored = true;
                break;
            }
        }
        if (targetIsland.getTower() != null && !towersAreIgnored) {
            partialResult.keySet().forEach(player -> {
                if (player.getSchoolBoard().getTowerType().equals(targetIsland.getTower()))
                    partialResult.put(player, partialResult.get(player) + targetIsland.getSize());
            });
        }
    }

    /**
     * This method is used to set a tower of a certain type on the island given; it calls the merge() method if needed.
     *
     * @param island The island on which the tower is going to be put.
     * @param tower  The TowerType of the tower to put.
     */
    public void setTowerOnIsland(Island island, TowerType tower) {
        island.setTower(tower);

        //Check if a merge to left is needed.
        boolean mergeDone;
        try {
            do {
                List<Island> islandsList = getIslands();
                mergeDone = false;
                for (Island currentIsland : islandsList) {
                    Island nextIsland = islandsList.get((islandsList.indexOf(currentIsland) + 1) % islandsList.size());
                    Log.debug("Current island " + currentIsland.getId());

                    Log.debug("Next id " + nextIsland.getId());


                    if (currentIsland.getId() != nextIsland.getId() && currentIsland.getTower() != null && nextIsland.getTower() != null && currentIsland.getTower().equals(nextIsland.getTower())) {
                        Log.debug("Current island tower " + currentIsland.getTower().toString());
                        Log.debug("Next island tower " + nextIsland.getTower().toString());
                        merge(currentIsland, nextIsland);
                        mergeDone = true;
                        break;
                    }
                }
            } while (mergeDone);
        } catch (Exception e) {
            Log.warning(e);
        }
    }

    /**
     * This method merges two islands on the left, increasing the size and the students on the resulting island.
     *
     * @param leftIsland  The island hosting the merge.
     * @param rightIsland The island merging with the island on it's left.
     */
    private void merge(Island leftIsland, Island rightIsland) {
        leftIsland.setSize(leftIsland.getSize() + rightIsland.getSize());
        if (rightIsland.isBanned()) leftIsland.setBan();
        Map<HouseColor, Integer> rightStudents = rightIsland.getStudents();

        for (Map.Entry<HouseColor, Integer> color : rightStudents.entrySet())
            for (int i = 0; i < color.getValue(); i++)
                leftIsland.addStudent(color.getKey());
        if (getMotherNatureIsland().getId() == rightIsland.getId()) this.motherNatureIsland = leftIsland;
        this.islands.remove(rightIsland);

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
        return new EnumMap<>(this.professors);
    }

    /**
     * The method give access to the data structure professors: the first is the HouseColor, the second is the Player who has the professor.
     *
     * @param color  The house color of the professor.
     * @param player The player who takes the professor.
     */
    public void setProfessor(HouseColor color, Player player) {
        this.professors.put(color, player);
    }

    /**
     * This method adds assistants to the data structure in the class GameBoard.
     *
     * @param currentPlayer   The current player that would like to play the card.
     * @param playedAssistant The card that the player would like to play.
     * @throws IllegalMoveException The played card is already played from another player.
     */
    public void addPlayedAssistant(Player currentPlayer, Assistant playedAssistant) throws IllegalMoveException {
        if (currentPlayer.getAssistants().size() > 1 && this.getPlayedAssistants().keySet().stream().anyMatch(player -> this.getPlayedAssistants().get(player).getId() == playedAssistant.getId())) {
            throw new IllegalMoveException();
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
     * @return The copy of the data structure playedAssistants.
     */
    public Map<Player, Assistant> getPlayedAssistants() {
        return new HashMap<>(this.playedAssistants);
    }

    /**
     * This method returns the player that has the bonus in the influence evaluation.
     *
     * @return The player with the bonus.
     */
    public Player getInfluenceBonus() {
        return this.influenceBonus;
    }

    /**
     * This method sets the player influences bonus.
     *
     * @param bonusPlayer The player that will receive the bonus.
     */
    public void setInfluenceBonus(Player bonusPlayer) {
        this.influenceBonus = bonusPlayer;
    }

    /**
     * This method returns the player that has the bonus in the count for the professors.
     *
     * @return The player with the bonus.
     */
    public Player getTieWinner() {
        return this.tieWinner;
    }

    /**
     * This method sets the player tie winner.
     *
     * @param bonusPlayer The player that will receive the bonus.
     */
    public void setTieWinner(Player bonusPlayer) {
        this.tieWinner = bonusPlayer;
    }

    /**
     * This method initializes the clouds, it is used in the constructor.
     */
    private void initializeClouds(int numPlayer) {
        for (int i = 0; i < numPlayer; i++) {
            this.clouds.add(new Cloud(i));
        }
    }

    /**
     * Standard redefinition of "equals" method.
     *
     * @param o Object to compare.
     * @return true if the two objects are the same.
     */
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || o.getClass() != this.getClass()) return false;

        GameBoard that = (GameBoard) o;
        return this.bag.equals(that.bag) && this.clouds.equals(that.clouds) && this.islands.equals(that.islands) && this.professors.equals(that.professors) && this.playedAssistants.equals(that.playedAssistants) && this.influenceBonus.equals(that.influenceBonus) && this.tieWinner.equals(that.tieWinner) && this.characters.equals(that.characters) && this.ignoreColor.equals(that.ignoreColor) && this.motherNatureIsland.equals(that.motherNatureIsland);
    }

    /**
     * Calculates the hash.
     *
     * @return The calculated hash.
     */
    @Override
    public int hashCode() {
        return Objects.hash(bag, clouds, islands, professors, playedAssistants, influenceBonus, tieWinner, characters, ignoreColor, motherNatureIsland);
    }
}
