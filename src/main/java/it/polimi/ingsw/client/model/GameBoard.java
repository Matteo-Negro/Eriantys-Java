package it.polimi.ingsw.client.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.TowerType;
import it.polimi.ingsw.utilities.parsers.JsonToObjects;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class GameBoard {
    private int motherNatureIsland;
    private String influenceBonus;
    private HouseColor ignoreColor;
    private List<Island> islands;
    private List<Cloud> clouds;
    private List<SpecialCharacter> specialCharacters;

    public GameBoard(int statusMotherNatureIsland, String statusInfluenceBonus, HouseColor statusIgnoreColor, JsonArray statusIslands, JsonArray statusClouds, JsonArray statusSpecialCharacters) {
        this.influenceBonus = statusInfluenceBonus;
        this.ignoreColor = statusIgnoreColor;
        this.parseClouds(statusClouds);
        this.parseSpecialCharacters(statusSpecialCharacters);
        this.parseIslands(statusIslands);
        this.motherNatureIsland = islands.get(statusMotherNatureIsland).getId();
        islands.get(statusMotherNatureIsland).hasMotherNature(true);
    }

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

                if (i == 0) this.islands.add(new Island((id + i) % 12, false, false, tower, null, false, ban));
                else {
                    this.islands.add(new Island((id + i) % 12, false, true, tower, null, false, ban));
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

    private void parseSpecialCharacters(JsonArray specialCharacters) {
        this.specialCharacters = new ArrayList<>();
        for (JsonElement character : specialCharacters) {
            int id = character.getAsJsonObject().get("id").getAsInt();
            int cost = character.getAsJsonObject().get("effectCost").getAsInt();
            boolean alreadyPaid = character.getAsJsonObject().get("alreadyPaid").getAsBoolean();
            boolean paidInRound = character.getAsJsonObject().get("paidInRound").getAsBoolean();
            boolean active = character.getAsJsonObject().get("active").getAsBoolean();
            switch (id) {
                case 1, 7, 11 -> {
                    JsonObject containedStudents = character.getAsJsonObject().get("containedStudents").getAsJsonObject();
                    this.specialCharacters.add(new SpecialCharacter(id, active, alreadyPaid, paidInRound, containedStudents, null, cost));
                }
                case 5 -> {
                    Integer availableBans = character.getAsJsonObject().get("availableBans").getAsInt();
                    this.specialCharacters.add(new SpecialCharacter(id, active, alreadyPaid, paidInRound, null, availableBans, cost));
                }
                default ->
                        this.specialCharacters.add(new SpecialCharacter(id, active, alreadyPaid, paidInRound, null, null, cost));
            }
        }
    }

    public List<Island> getIslands() {
        return new ArrayList<>(this.islands);
    }

    public List<Cloud> getClouds() {
        return new ArrayList<>(this.clouds);
    }

    public List<SpecialCharacter> getSpecialCharacters() {
        return new ArrayList<>(this.specialCharacters);
    }

    public SpecialCharacter getSpecialCharacterById(int id) {
        for (SpecialCharacter sc : getSpecialCharacters()) {
            if (sc.getId() == id) return sc;
        }

        return null;
    }

    public int getMotherNatureIsland() {
        return this.motherNatureIsland;
    }

    public Island getIslandById(int id) {
        for (Island isl : this.islands) {
            if (isl.getId() == id) return isl;
        }
        return null;
    }
}
