package it.polimi.ingsw.model.board.effects;

import java.util.Map;

public class PrincessEffect implements Effect{

    private Map<HouseColor, Integer> students;

    public PrincessEffect(){
        students = new Map<HouseColor, Integer>;
    }

    public int getId(){
        return 11;
    }

    public void effect(){

    }

    public void clean(){

    }

    public int setCost(){
        return 2;
    }

    private Map<HouseColor, Integer> getStudents(){
        return students;
    }

    private void takeStudent(){

    }

    private void addStudent(HouseColor color){

    }
}
