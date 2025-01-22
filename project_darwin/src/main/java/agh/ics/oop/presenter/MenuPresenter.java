package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationApp;
import agh.ics.oop.model.info.Constants;
import agh.ics.oop.model.info.ConstantsList;
import com.sun.javafx.scene.control.IntegerField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;


//TODO: ogarnac new game zapisywanie configu i import configu i wogle stestowanie czy to dziala
public class MenuPresenter extends BasePresenter {

    @FXML
    private IntegerField numberOfGenes;
    @FXML
    private IntegerField startingAnimalEnergy;
    @FXML
    private IntegerField energyFromPlant;
    @FXML
    private IntegerField mapWidth;
    @FXML
    private IntegerField mapHeight;
    @FXML
    private CheckBox goodHarvest;
    @FXML
    private CheckBox completeRandom;
    @FXML
    private IntegerField numberOfAnimals;
    @FXML
    private IntegerField numberOfPlants;
    @FXML
    private IntegerField geneomeLength;
    @FXML
    private IntegerField minMutations;
    @FXML
    private IntegerField maxMutations;
    @FXML
    private IntegerField plantsPerDay;
    @FXML
    private IntegerField minimalBreedingEnergy;
    @FXML
    private IntegerField energyLostForReproduction;
    @FXML
    private IntegerField energyLostPerDay;

    @FXML
    private CheckBox formExportStatistics;

    @FXML
    private Button createGame;

    @FXML
    private Button exportConfiguration;

    @FXML
    private Button importConfiguration;
    private static AtomicInteger simulationId = new AtomicInteger(1);
    private SimulationApp app;

    @FXML
    private void initialize() throws Exception {
        createGame.setOnAction(event -> {
            try {
                var id = simulationId.getAndIncrement();
                createNewGame(id);
            } catch (Exception e) {
                showAlert("Error", "Invalid data in game parameters", "Cannot start the Darwin World", Alert.AlertType.ERROR);
            }
        });

        exportConfiguration.setOnAction(event -> {
            try {
                saveConfig();
            } catch (Exception e) {
                showAlert("Error", "Error on saving configuration", "Cannot export the configuration file", Alert.AlertType.ERROR);
            }
        });

        importConfiguration.setOnAction(event -> {
            try {
                fileSelector();
            } catch (Exception e) {
                showAlert("Error", "Error on importing configuration", "Cannot load the configuration file", Alert.AlertType.ERROR);
            }
        });
    }

    public void setApp(SimulationApp app){
        this.app = app;
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg);
        a.show();
    }

    private void setUpConstants(int id) {
        Constants constants = new Constants (
                numberOfGenes.getValue(),
                startingAnimalEnergy.getValue(),
                energyFromPlant.getValue(),
                mapWidth.getValue(),
                mapHeight.getValue(),
                goodHarvest.isSelected(),
                completeRandom.isSelected(),
                numberOfAnimals.getValue(),
                numberOfPlants.getValue(),
//                geneomeLength.getValue(),
                minMutations.getValue(),
                maxMutations.getValue(),
                plantsPerDay.getValue(),
                minimalBreedingEnergy.getValue(),
                energyFromPlant.getValue(),
                energyLostForReproduction.getValue()
        );

        ConstantsList.addToConstantsList(id, constants);
    }
    private boolean isValidData() {

        if(numberOfGenes.getValue() == 0) {
            showError("Too small gene size");
            return false;
        }
        if(minMutations.getValue() > numberOfGenes.getValue()) {
            showError("Min mutation number higher than length of gene");
            return false;
        }
        if(maxMutations.getValue() > numberOfGenes.getValue()) {
            showError("Max mutation number higher than length of gene");
            return false;
        }
        if(maxMutations.getValue() < minMutations.getValue()) {
            showError("Max mutation number lower than min mutation number");
            return false;
        }
        if(minimalBreedingEnergy.getValue() < energyLostForReproduction.getValue()) {
            showError("Energy to be ready for reproduction is lower than used during reproduction");
            return false;
        }
        if(numberOfAnimals.getValue() < 2) {
            showError("Starting animal number is too little. Need at least 2 animals to survive");
            return false;
        }
        if(energyFromPlant.getValue() == 0) {
            showError("Plants should give some energy when eaten");
            return false;
        }
        if(mapWidth.getValue() == 0) {
            showError("Map width has to be at least 1");
            return false;
        }
        if(mapHeight.getValue() == 0) {
            showError("Map height has to be at least 1");
            return false;
        }

        return true;
    }
    private void setFields(String[] vals) {
        numberOfGenes.setValue(Integer.parseInt(vals[0]));
        startingAnimalEnergy.setValue(Integer.parseInt(vals[1]));
        energyFromPlant.setValue(Integer.parseInt(vals[2]));
        mapWidth.setValue(Integer.parseInt(vals[3]));
        mapHeight.setValue(Integer.parseInt(vals[4]));
        goodHarvest.setSelected(Boolean.parseBoolean(vals[5]));
        completeRandom.setSelected(Boolean.parseBoolean(vals[6]));
        numberOfAnimals.setValue(Integer.parseInt(vals[7]));
        numberOfPlants.setValue(Integer.parseInt(vals[8]));
        geneomeLength.setValue(Integer.parseInt(vals[9]));
        minMutations.setValue(Integer.parseInt(vals[10]));
        maxMutations.setValue(Integer.parseInt(vals[11]));
        plantsPerDay.setValue(Integer.parseInt(vals[12]));
        minimalBreedingEnergy.setValue(Integer.parseInt(vals[13]));
        energyLostForReproduction.setValue(Integer.parseInt(vals[14]));
        energyLostPerDay.setValue(Integer.parseInt(vals[15]));
    }

    public void saveConfig() {
        if(!isValidData()) {
            showError("Provide valid data before saving");
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        File resourcesDirectory = new File("src/main/resources/configs/");
        fileChooser.setInitialDirectory(resourcesDirectory);

        Stage stage = (Stage) exportConfiguration.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);
        saveConfigData(file);
    }

    private void saveConfigData(File file) {
        try(FileWriter writer = new FileWriter(file)) {
            writer.write(numberOfGenes.getValue() + "\n");
            writer.write(startingAnimalEnergy.getValue() + "\n");
            writer.write(energyFromPlant.getValue() + "\n");
            writer.write(mapWidth.getValue() + "\n");
            writer.write(mapHeight.getValue() + "\n");
            writer.write(goodHarvest.isSelected() + "\n");
            writer.write(completeRandom.isSelected() + "\n");
            writer.write(numberOfAnimals.getValue() + "\n");
            writer.write(numberOfPlants.getValue() + "\n");
            writer.write(geneomeLength.getValue() + "\n");
            writer.write(minMutations.getValue() + "\n");
            writer.write(maxMutations.getValue() + "\n");
            writer.write(plantsPerDay.getValue() + "\n");
            writer.write(minimalBreedingEnergy.getValue() + "\n");
            writer.write(energyLostForReproduction.getValue() + "\n");
            writer.write(energyLostPerDay.getValue() + "\n");
        } catch (IOException e) {
            showError("Something went wrong");

        }
    }


    public void fileSelector() {
        Stage newWindow = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File resourcesDirectory = new File("src/main/resources/configs/");
        fileChooser.setInitialDirectory(resourcesDirectory);


        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(newWindow);
        if(file == null) {
            return;
        }

        String[] lines = new String[17];
        int i = 0;
        try(Scanner scanner = new Scanner(file)) {
            while(scanner.hasNextLine()) {
                if(i >= 17) {
                    break;
                }
                String line = scanner.nextLine();
                lines[i] = line;
                i++;
            }
        }
        catch (IOException e) {
            showError("Wrong file format selected. Make sure config is alright");
            return;
        }

        if(i != 16) {
            showError("Wrong file length. File should be 14 lines long!");
            return;
        }

        try {
            setFields(lines);
        } catch (NumberFormatException e) {
            showError("Wrong file format. Some values are not integers");
        }

    }

    private void createNewGame(int id) throws Exception {
        if(!isValidData()) {
            System.out.println("InvalidData");
            return;}

        FXMLLoader loader = new FXMLLoader();
        Stage stage = new Stage();


        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
        BorderPane view = loader.load();
        SimulationPresenter presenter = loader.getController();

        loader.setController(presenter);

        setUpConstants(id);
        //Simulation simulation = new Simulation(id,1);

        presenter.setUp(id, app, stage);
        presenter.run();


        stage.setTitle("Darwin World - World " + id);
        stage.setScene(new Scene(view));
        stage.setMaximized(true);
        stage.show();

    }


}