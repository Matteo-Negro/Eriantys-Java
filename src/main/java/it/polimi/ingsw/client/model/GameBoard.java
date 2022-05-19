package it.polimi.ingsw.client.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import it.polimi.ingsw.utilities.HouseColor;

import java.util.*;

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

        this.clouds = new ArrayList<>();
        Map <HouseColor, Integer> students;
        for(JsonElement map : statusClouds){
            students = new EnumMap<>(HouseColor.class);
            for(String color : map.getAsJsonObject().keySet()){
                students.put(HouseColor.valueOf(color), map.getAsJsonObject().get("color").getAsInt());
            }
            Cloud cloud = new Cloud(students);
            this.clouds.add(cloud);
        }

        //TODO parse islands and characters
        /*this.islands = new ArrayList<>(statusIslands);
        this.specialCharacters = new ArrayList<>(statusSpecialCharacters);*/
    }
}
