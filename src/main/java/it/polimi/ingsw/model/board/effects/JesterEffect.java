package it.polimi.ingsw.model.board.effects;

import java.util.Map;

public class JesterEffect implements Effect{

    private Map<HouseColor, Integer> students;


    public JesterEffect(){
        students = new Map<HouseColor, Integer>;
    }

    public int getId(){
        return 7;
    }

    public void effect(Map<HouseColor, Integer> exchangedStudents){
        exchangeStudents(exchangedStudents);
    }

    public void clean(){
        students = new Map<HouseColor, Integer>();
    }

    public int getCost(){
        return 1;
    }

    private Map<HouseColor, Integer> getStudents(){
        return students;
    }

    private void exchangeStudents(Map<HouseColor, Integer> exchangedStudents){
        students = exchangedStudents;
    }
}
