package it.polimi.ingsw.model.board.effects;

import java.util.Map;

public class PrincessEffect implements Effect{

    private int cost;

    private Map<HouseColor, Integer> students;

    public PrincessEffect(){
        students = new Map<HouseColor, Integer>;
        cost = 2;
    }

    public int getId(){
        return 11;
    }

    public int getCost(){
        return cost;
    }

    public void effect(){

    }

    public void clean(){

    }

    private Map<HouseColor, Integer> getStudents(){
        return students;
    }

    private void takeStudent(){

    }

    private void addStudent(HouseColor color){

    }
}
