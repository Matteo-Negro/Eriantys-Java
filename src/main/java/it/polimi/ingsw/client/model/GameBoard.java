package it.polimi.ingsw.client.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import it.polimi.ingsw.utilities.HouseColor;
import it.polimi.ingsw.utilities.TowerType;

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
        this.motherNatureIsland = statusMotherNatureIsland;
        this.influenceBonus = statusInfluenceBonus;
        this.ignoreColor = statusIgnoreColor;
        this.parseClouds(statusClouds);
        this.parseSpecialCharacters(statusSpecialCharacters);
        this.parseIslands(statusIslands);
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
        int id = 0;
        for (JsonElement island : islands) {
            int size = island.getAsJsonObject().get("size").getAsInt();
            TowerType tower = null;
            if (!(island.getAsJsonObject().get("tower") instanceof JsonNull))
                tower = TowerType.valueOf(island.getAsJsonObject().get("tower").getAsString());
            boolean ban = island.getAsJsonObject().get("ban").getAsBoolean();
            JsonObject containedStudents = island.getAsJsonObject().get("students").getAsJsonObject();
            boolean motherNature;
            motherNature = id == this.motherNatureIsland;
            for (int i = 0; i < size; i++) {
                if (i == 0) this.islands.add(new Island(false, false, tower, containedStudents, motherNature, ban));
                else {
                    this.islands.add(new Island(false, true, tower, null, motherNature, ban));
                    this.islands.get(id + i - 1).setNext();
                }
            }
            id += size;
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
}
