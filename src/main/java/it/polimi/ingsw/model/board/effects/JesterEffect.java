package it.polimi.ingsw.model.board.effects;

public class JesterEffect implements Effect{

    private Map<HouseColor, Integer> students;

    private int cost;

    public JesterEffect(){
        students = new Map<HouseColor, Integer>;
        cost = 1;
    }

    public int getId(){
        return 7;
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

    private void exchangeStudents(){

    }
}
