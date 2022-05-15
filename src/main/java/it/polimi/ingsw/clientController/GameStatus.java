package it.polimi.ingsw.clientController;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class GameStatus {
    private Map<String, String> waitingRoom;
    private int playersNumber;
    private boolean expert;
    private Player currentPlayer;

    public GameStatus(Map<String, String> waitingRoom){
        this.waitingRoom = new HashMap<>(waitingRoom);
        this.playersNumber = waitingRoom.keySet().size();
        this.currentPlayer = null;
    }

    public GameStatus(JsonObject status){

    }

    public Map<String, String> getWaitingRoom(){
        return new HashMap<>(this.waitingRoom);
    }
}
