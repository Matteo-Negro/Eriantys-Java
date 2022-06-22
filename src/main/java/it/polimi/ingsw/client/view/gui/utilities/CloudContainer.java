package it.polimi.ingsw.client.view.gui.utilities;

import it.polimi.ingsw.utilities.HouseColor;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a simple interface to manage a GUI cloud. Provides all the required methods to create and update it.
 */
public class CloudContainer {

    private Parent pane;
    private List<ImageView> students;

    /**
     * Default class constructor.
     */
    CloudContainer() {
        pane = null;
        students = null;
    }

    /**
     * Gets the pane to print on the screen.
     *
     * @return The pane with all the graphics.
     */
    public Parent getPane() {
        return pane;
    }

    /**
     * Initializes the pane.
     *
     * @param pane The pane to add.
     */
    void setPane(Parent pane) {
        this.pane = pane;
    }

    /**
     * Updates the student on the cloud.
     *
     * @param students The List of students on the cloud.
     */
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

    /**
     * Sets the list of students' images on the cloud.
     *
     * @param student The ImageView to add.
     */
    void addStudent(ImageView student) {
        if (students == null)
            students = new ArrayList<>();
        students.add(student);
    }
}
