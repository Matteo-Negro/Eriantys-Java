package it.polimi.ingsw.client.view.gui.utilities;

import it.polimi.ingsw.client.model.Island;
import it.polimi.ingsw.utilities.HouseColor;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

public class Grids {

    private Grids() {
    }

    static GridPane island(Island island, IslandContainer islandContainer) {

        GridPane gridPane = new GridPane();
        gridPane.setGridLinesVisible(true);
        gridPane.setPrefWidth(162);
        gridPane.setPrefHeight(156);

        ObservableList<RowConstraints> rows = gridPane.getRowConstraints();
        ObservableList<ColumnConstraints> columns = gridPane.getColumnConstraints();

        for (int index = 0; index < 3; index++) {
            rows.add(new RowConstraints());
            columns.add(new ColumnConstraints());
        }

        for (int index = 0; index < 3; index++) {
            rows.get(index).setVgrow(Priority.SOMETIMES);
            columns.get(index).setHgrow(Priority.SOMETIMES);
        }

        rows.get(0).setPrefHeight(30);
        rows.get(1).setPrefHeight(30);
        rows.get(2).setPrefHeight(10);
        columns.get(0).setPrefWidth(20);
        columns.get(1).setPrefWidth(100);
        columns.get(2).setPrefWidth(20);

        gridPane.add(islandTowerMotherNature(island, islandContainer), 1, 0);
        gridPane.add(islandStudents(island, islandContainer), 1, 1);

        return gridPane;
    }

    private static GridPane islandTowerMotherNature(Island island, IslandContainer islandContainer) {

        GridPane gridPane = new GridPane();
        gridPane.setGridLinesVisible(true);

        ObservableList<RowConstraints> rows = gridPane.getRowConstraints();
        ObservableList<ColumnConstraints> columns = gridPane.getColumnConstraints();

        rows.add(new RowConstraints());
        columns.add(new ColumnConstraints());
        columns.add(new ColumnConstraints());

        rows.get(0).setVgrow(Priority.SOMETIMES);
        columns.get(0).setHgrow(Priority.SOMETIMES);
        columns.get(1).setHgrow(Priority.SOMETIMES);

        rows.get(0).setValignment(VPos.BOTTOM);
        columns.get(0).setHalignment(HPos.CENTER);
        columns.get(1).setHalignment(HPos.CENTER);

        ImageView tower = Images.tower(island.getTower());
        islandContainer.setTower(tower);
        gridPane.add(tower, 0, 0);

        ImageView motherNature = Images.motherNature(island.hasMotherNature());
        islandContainer.setMotherNature(motherNature);
        gridPane.add(motherNature, 1, 0);

        return gridPane;
    }

    private static GridPane islandStudents(Island island, IslandContainer islandContainer) {

        GridPane gridPane = new GridPane();
        gridPane.setGridLinesVisible(true);

        ObservableList<RowConstraints> rows = gridPane.getRowConstraints();
        ObservableList<ColumnConstraints> columns = gridPane.getColumnConstraints();

        rows.add(new RowConstraints());
        rows.add(new RowConstraints());
        columns.add(new ColumnConstraints());

        rows.get(0).setVgrow(Priority.SOMETIMES);
        rows.get(1).setVgrow(Priority.SOMETIMES);
        columns.get(0).setHgrow(Priority.SOMETIMES);

        gridPane.add(islandStudents(0, island, islandContainer), 0, 0);
        gridPane.add(islandStudents(1, island, islandContainer), 0, 1);

        return gridPane;
    }

    private static GridPane islandStudents(int row, Island island, IslandContainer islandContainer) {

        GridPane gridPane = new GridPane();
        gridPane.setGridLinesVisible(true);

        ObservableList<RowConstraints> rows = gridPane.getRowConstraints();
        ObservableList<ColumnConstraints> columns = gridPane.getColumnConstraints();

        rows.add(new RowConstraints());
        rows.get(0).setVgrow(Priority.SOMETIMES);

        for (int index = 0; index < 3 - row; index++) {
            columns.add(new ColumnConstraints());
            columns.get(index).setHgrow(Priority.SOMETIMES);
        }

        if (row == 0) {
            gridPane.add(Boxes.islandStudent(HouseColor.GREEN, island.getStudents().get(HouseColor.GREEN), islandContainer), 0, 0);
            gridPane.add(Boxes.islandStudent(HouseColor.RED, island.getStudents().get(HouseColor.RED), islandContainer), 1, 0);
            gridPane.add(Boxes.islandStudent(HouseColor.YELLOW, island.getStudents().get(HouseColor.YELLOW), islandContainer), 2, 0);
        } else {
            gridPane.add(Boxes.islandStudent(HouseColor.FUCHSIA, island.getStudents().get(HouseColor.FUCHSIA), islandContainer), 0, 0);
            gridPane.add(Boxes.islandStudent(HouseColor.BLUE, island.getStudents().get(HouseColor.BLUE), islandContainer), 1, 0);
        }

        return gridPane;
    }
}
