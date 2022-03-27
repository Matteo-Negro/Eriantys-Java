package it.polimi.ingsw.model.board.effects;

public class JesterEffect implements Effect{

    private Map<HouseColor, Integer> students;

    public JesterEffect(){
        students = new Map<HouseColor, Integer>;
    }

    public int getId(){
        return 7;
    }

    public void effect(){

    }

    public void clean(){

    }

    public int setCost(){
        return 1;
    }

    private Map<HouseColor, Integer> getStudents(){
        return students;
    }

    private void exchangeStudents(){

    }
}
