package it.polimi.ingsw.model.board.effects;

public class MonkEffect implements Effect{

    private Map<HouseColor, Integer> students;
    private int cost;

    public MonkEffect(){
        students = new Map<HouseColor, Integer>;
        cost = 1;
    }

    public int getId(){
        return 1;
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

    private void addStudent(HouseColor color){

    }

    private void takeStudent(HouseColor color){

    }
}
