package agh.ics.oop.presenter;

import agh.ics.oop.SimulationApp;
import agh.ics.oop.model.creatures.Animal;
import agh.ics.oop.model.util.Vector2d;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class AnimalsList {

    @FXML
    private GridPane table;
    @FXML
    private Button closeButton;
    private CheckBox settedCheckBox = null;
    private SimulationPresenter simulationPresenter;

    private void checkBoxClick (CheckBox checkBox, Animal animal){
        if (settedCheckBox != null){
            settedCheckBox.setSelected(false);
        }
        if (settedCheckBox == checkBox){
            settedCheckBox = null;
            simulationPresenter.setMarkedAnimal(null);
        }
        else {
            settedCheckBox = checkBox;
            simulationPresenter.setMarkedAnimal(animal);
        }
    }

    private void createButtonItem(){
        closeButton.setText("Zakoncz");
        closeButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        closeButton.setOnAction(event -> {
            Platform.runLater(() -> {
                Stage stage = (Stage) closeButton.getScene().getWindow();
                stage.close();
            });
        });
    }

    private CheckBox createCheckBoxItem(Animal animal, Animal markedAnimal){
        CheckBox item = new CheckBox();
        GridPane.setHalignment(item, HPos.CENTER);
        item.setPadding(new Insets(5, 5, 5, 5));
        item.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        if (markedAnimal != null && animal.getId() == markedAnimal.getId()){
            item.setSelected(true);
            settedCheckBox = item;
        }
        item.setOnAction(event -> {
            Platform.runLater(() -> {
                checkBoxClick(item, animal);
            });
        });
        return item;
    }

    private Label createStringItem(String content){
        Label item = new Label(String.valueOf(content));
        GridPane.setHalignment(item, HPos.CENTER);
        item.setPadding(new Insets(5, 5, 5, 5));
        item.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        return item;
    }

    private Label createIntegerItem(Integer content){
        Label item = new Label(String.valueOf(content));
        GridPane.setHalignment(item, HPos.CENTER);
        item.setPadding(new Insets(5, 5, 5, 5));
        item.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        return item;
    }

    private Label createUUIDItem(UUID content){
        Label item = new Label(String.valueOf(content));
        GridPane.setHalignment(item, HPos.CENTER);
        item.setPadding(new Insets(5, 5, 5, 5));
        item.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        return item;
    }

    public void setUp(List<Animal> listOfAnimals, SimulationPresenter simulationPresenter, Animal markedAnimal) {
        this.simulationPresenter = simulationPresenter;
        createButtonItem();
        table.getChildren().retainAll(table.getChildren().get(0)); // hack to retain visible grid lines
        table.getColumnConstraints().clear();
        table.getRowConstraints().clear();
        table.add(createStringItem("Zaznacz"), 0, 0);
        table.add(createStringItem("Orientacja"), 1, 0);
        table.add(createStringItem("Poziom energii"), 2, 0);
        table.add(createStringItem("Wiek"), 3, 0);
        table.add(createStringItem("Liczba dzieci"), 4, 0);
        table.add(createStringItem("ID"), 5, 0);
        for (int i = 1; i <= listOfAnimals.size(); i++){
            table.add(createCheckBoxItem(listOfAnimals.get(i-1), markedAnimal),0,i);
            table.add(createStringItem(listOfAnimals.get(i-1).getDirection().toString()),1,i);
            table.add(createIntegerItem(listOfAnimals.get(i-1).getEnergy()),2,i);
            table.add(createIntegerItem(listOfAnimals.get(i-1).getAge()),3,i);
            table.add(createIntegerItem(listOfAnimals.get(i-1).getChildrenNumber()),4,i);
            table.add(createUUIDItem(listOfAnimals.get(i-1).getId()),5,i);
        }
    }
}