package it.polimi.ingsw.client.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.Log;
import it.polimi.ingsw.utilities.TowerType;
import it.polimi.ingsw.utilities.parsers.JsonToObjects;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents the game board into the game model.
 *
 * @author Riccardo Milici
 */
public class GameBoard {
    private final int motherNatureIsland;
    private final String influenceBonus;
    private final HouseColor ignoreColor;
    private List<Island> islands;
    private List<Cloud> clouds;
    private List<SpecialCharacter> specialCharacters;

    /**
     * Default class constructor.
     *
     * @param statusMotherNatureIsland The id of the island on which mother nature is placed.
     * @param statusInfluenceBonus     The name of the player with an influence bonus.
     * @param statusIgnoreColor        The color to ignore during the resolution of an island.
     * @param statusIslands            The islands.
     * @param statusClouds             The clouds.
     * @param statusSpecialCharacters  The special characters.
     */
    public GameBoard(int statusMotherNatureIsland, String statusInfluenceBonus, HouseColor statusIgnoreColor, JsonArray statusIslands, JsonArray statusClouds, JsonArray statusSpecialCharacters) {
        this.influenceBonus = statusInfluenceBonus;
        this.ignoreColor = statusIgnoreColor;
        this.motherNatureIsland = statusMotherNatureIsland;
        Log.debug("MotherNature is on island " + this.motherNatureIsland);
        this.parseClouds(statusClouds);
        this.parseSpecialCharacters(statusSpecialCharacters);
        this.parseIslands(statusIslands);
    }

    /**
     * This method creates the clouds instances and initializes them through the info contained into the given JsonArray.
     *
     * @param clouds The JsonArray cto parse.
     */
    private void parseClouds(JsonArray clouds) {
        this.clouds = new ArrayList<>();
        Map<HouseColor, Integer> students;
        for (JsonElement map : clouds) {
            students = new EnumMap<>(HouseColor.class);
            for (HouseColor color : HouseColor.values())
                students.put(color, map.getAsJsonObject().get(color.name()).getAsInt());
            Cloud cloud = new Cloud(students);
            this.clouds.add(cloud);
        }
    }

    /**
     * This method creates the island instances and initializes them through the info contained into the given JsonArray.
     *
     * @param islands The JsonArray to parse.
     */
    private void parseIslands(JsonArray islands) {
        this.islands = new ArrayList<>();

        for (JsonElement island : islands) {
            int id = island.getAsJsonObject().get("id").getAsInt();
            int size = island.getAsJsonObject().get("size").getAsInt();
            TowerType tower = null;
            if (!(island.getAsJsonObject().get("tower") instanceof JsonNull))
                tower = TowerType.valueOf(island.getAsJsonObject().get("tower").getAsString());

            boolean ban = island.getAsJsonObject().get("ban").getAsBoolean();
            JsonObject containedStudents = island.getAsJsonObject().get("students").getAsJsonObject();
            Map<HouseColor, Integer> students = JsonToObjects.parseStudents(containedStudents);

            int studentsNumber = 0;
            for (HouseColor color : HouseColor.values())
                studentsNumber = studentsNumber + students.get(color);

            for (int i = 0; i < size; i++) {
                boolean hasMotherNature = getMotherNatureIsland() == ((id + i) % 12);

                if (i == 0)
                    this.islands.add(new Island((id + i) % 12, false, false, tower, null, hasMotherNature, ban));
                else {
                    this.islands.add(new Island((id + i) % 12, false, true, tower, null, hasMotherNature, ban));
                    getIslandById((id + i - 1) % 12).setNext();
                }
            }

            int i = 0;
            while (studentsNumber > 0) {
                for (HouseColor color : HouseColor.values()) {
                    if (students.get(color) > 0) {
                        students.put(color, students.get(color) - 1);
                        this.getIslandById((id + i) % 12).addStudent(color);
                        studentsNumber--;
                        break;
                    }
                }
                i = (i + 1) % size;
            }
        }
    }

    /**
     * This method instantiates and initializes the special characters through the info contained into the given JsonArray.
     *
     * @param specialCharacters The JsonArray to parse.
     */
    private void parseSpecialCharacters(JsonArray specialCharacters) {
        this.specialCharacters = new ArrayList<>();
        for (JsonElement character : specialCharacters) {
            int id = character.getAsJsonObject().get("id").getAsInt();
            int cost = character.getAsJsonObject().get("effectCost").getAsInt();
            boolean alreadyPaid = character.getAsJsonObject().get("alreadyPaid").getAsBoolean();
            boolean paidInTurn = character.getAsJsonObject().get("paidInTurn").getAsBoolean();
            boolean active = character.getAsJsonObject().get("active").getAsBoolean();
            int usesNumber = character.getAsJsonObject().get("usesNumber").getAsInt();
            switch (id) {
                case 1, 7, 11 -> {
                    JsonObject containedStudents = character.getAsJsonObject().get("containedStudents").getAsJsonObject();
                    this.specialCharacters.add(new SpecialCharacter(id, active, alreadyPaid, paidInTurn, containedStudents, null, cost, usesNumber));
                }
                case 5 -> {
                    Integer availableBans = character.getAsJsonObject().get("availableBans").getAsInt();
                    this.specialCharacters.add(new SpecialCharacter(id, active, alreadyPaid, paidInTurn, null, availableBans, cost, usesNumber));
                }
                default ->
                        this.specialCharacters.add(new SpecialCharacter(id, active, alreadyPaid, paidInTurn, null, null, cost, usesNumber));
            }
        }
    }

    /**
     * Gets a copy of the list containing the islands.
     *
     * @return A new ArrayList containing a copy of the islands attribute.
     */
    public List<Island> getIslands() {
        return new ArrayList<>(this.islands);
    }

    /**
     * Gets a copy of the list containing the clouds.
     *
     * @return A new ArrayList containing a copy of the clouds attribute.
     */
    public List<Cloud> getClouds() {
        return new ArrayList<>(this.clouds);
    }

    /**
     * Gets a copy of the list containing the special characters.
     *
     * @return A new ArrayList containing a copy of the specialCharacters attribute.
     */
    public List<SpecialCharacter> getSpecialCharacters() {
        return new ArrayList<>(this.specialCharacters);
    }

    /**
     * Gets the special characters having the specified id.
     *
     * @param id The special character's id.
     * @return The special character searched if present, null otherwise.
     */
    public SpecialCharacter getSpecialCharacterById(int id) {
        for (SpecialCharacter sc : getSpecialCharacters()) {
            if (sc.getId() == id) return sc;
        }
        return null;
    }

    /**
     * Gets the id of the island on which mother nature is currently placed.
     *
     * @return The motherNatureIsland attribute.
     */
    public int getMotherNatureIsland() {
        return this.motherNatureIsland;
    }

    /**
     * Gets the island having the specified id.
     *
     * @param id The island's id.
     * @return The island searched if present, null otherwise.
     */
    public Island getIslandById(int id) {
        for (Island isl : getIslands()) {
            if (isl.getId() == id) return isl;
        }
        return null;
    }

    /**
     * Returns true if a special character has been paid during this round.
     *
     * @return True if a special character has been paid during this turn, false otherwise.
     */
    public boolean characterPaidInTurn() {
        if (this.specialCharacters != null) {
            for (SpecialCharacter character : getSpecialCharacters()) {
                if (character.isPaidInTurn())
                    return true;
            }
        }
        return false;
    }
}
