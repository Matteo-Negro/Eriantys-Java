package it.polimi.ingsw.client.view.gui.utilities;

import it.polimi.ingsw.utilities.HouseColor;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class CloudContainer {

    private Parent pane;
    private List<ImageView> students;

    CloudContainer() {
        pane = null;
        students = null;
    }

    public Parent getPane() {
        return pane;
    }

    void setPane(Parent pane) {
        this.pane = pane;
    }

    public void updateStudents(List<HouseColor> students) {
        if (students == null)
            Platform.runLater(() -> {
                for (ImageView student : this.students)
                    student.setVisible(false);
            });
        else
            Platform.runLater(() -> {
                for (int index = 0; index < this.students.size(); index++) {
                    this.students.get(index).setImage(Images.getStudent3dByColor(students.get(index)));
                    this.students.get(index).setVisible(true);
                }
            });
    }

    void addStudent(ImageView student) {
        if (students == null)
            students = new ArrayList<>();
        students.add(student);
    }
}
