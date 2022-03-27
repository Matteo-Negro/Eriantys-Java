package it.polimi.ingsw.model.board.effects;

public class MonkEffect implements Effect{

    private Map<HouseColor, Integer> students;
    private int cost;

    public MonkEffect(){
        students = new Map<HouseColor, Integer>();
    }

    public int getId(){
        return 1;
    }

    public void effect(HouseColor color){
        takeStudent(color);
    }

    public void clean(){

    }

    public int getCost(){
        return 1;
    }

    private Map<HouseColor, Integer> getStudents(){
        return students;
    }

    private void addStudent(HouseColor color){

    }

    private void takeStudent(HouseColor color){

    }
}
