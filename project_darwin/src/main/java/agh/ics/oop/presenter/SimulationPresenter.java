package agh.ics.oop.presenter;

import agh.ics.oop.SimulationApp;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.Simulation;
import agh.ics.oop.model.ConsoleMapDisplay;
import agh.ics.oop.model.ChangeListener;
import agh.ics.oop.model.creatures.Animal;
import agh.ics.oop.model.info.Constants;
import agh.ics.oop.model.info.ConstantsList;
import agh.ics.oop.model.maps.WorldMap;
import agh.ics.oop.model.util.CSVWriter;
import agh.ics.oop.model.util.Vector2d;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class SimulationPresenter implements ChangeListener {

    @FXML
    private GridPane mapGrid;
    @FXML
    private Button resumeButton;
    @FXML
    private Button pauseButton;
    @FXML
    private Button saveButton;
    @FXML
    private TextField saveFilePath;
    @FXML
    private Label numberOfPlantsLabel = new Label();
    @FXML
    private Label numberOfAnimalsLabel = new Label();
    @FXML
    private Label numberOfEmptyFieldsLabel = new Label();
    @FXML
    private Label  averageEnergyLabel = new Label();
    @FXML
    private Label averageDaysOfLivingLabel = new Label();
    @FXML
    private Label averageChildrenNumberLabel = new Label();
    @FXML
    private VBox averageEnergyVBox = new VBox();
    @FXML
    private VBox averageDaysOfLivingVBox = new VBox();
    @FXML
    private VBox averageChildrenNumberVBox = new VBox();
    @FXML
    private Label  genotype0 = new Label();
    @FXML
    private Label  genotype1 = new Label();
    @FXML
    private Label  genotype2 = new Label();
    @FXML
    private Label  genotype3 = new Label();
    @FXML
    private Label  genotype4 = new Label();
    @FXML
    private Label  genotype5 = new Label();
    @FXML
    private Label  genotype6 = new Label();
    @FXML
    private Label  genotype7 = new Label();
    static final int CELL_WIDTH = 40;
    static final int CELL_HEIGHT = 40;
    private Simulation simulation;
    private int simulationId;
    private Stage stage;
    private WorldMap worldMap;
    private Constants constants;
    private SimulationEngine engine;
    private SimulationApp app;
    private Animal markedAnimal;
    private final HashMap<Vector2d, Button> buttonMap = new HashMap<>();
    private HashSet<Animal> popularGeneAnimals;


    public void run() throws InterruptedException {
        engine.runAsync();
    }

    public SimulationPresenter() {}

    public void setUp(int simulationId, SimulationApp app, Stage stage) {
        this.app = app;

        this.simulationId = simulationId;
        this.stage = stage;
        this.constants = ConstantsList.getConstants(simulationId);

        this.simulation = new Simulation(this.simulationId, 1000);
        this.worldMap = simulation.getMap();

        this.engine = getSimulationEngine(simulation);
        Platform.runLater(() -> {
                changeStats(worldMap);
                drawMap(worldMap);

        });
        resumeButton.setOnAction((e) -> simulation.resume());
        pauseButton.setOnAction((e) -> simulation.pause());
        saveButton.setOnAction((e) -> this.setUpWriter());

        stage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::exitApplication);
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        if(simulationId == -1) throw new RuntimeException("Didn't set-up UI presenter before calling mapChanged");
        Platform.runLater(() -> {
            changeStats(worldMap);
            drawMap(worldMap);
        });
    }

    private void changeStats (WorldMap worldMap){
        System.out.println("Stats Changed");
        numberOfAnimalsLabel.setText("Liczba zyjacyh zwierzat: " + worldMap.getTotalAnimals());
        numberOfPlantsLabel.setText("Aktualna liczba roslin: " + worldMap.getTotalPlants());
        //numberOfEmptyFieldsLabel.setText("Aktualna liczba pustych pol: " + worldMap.getNumberOfEmptyFields());
        averageEnergyLabel.setText("Sredni poziom energii dla zyjacych zwierzat: " + worldMap.getAverageEnergy());
        averageEnergyVBox.setVisible(worldMap.getAverageEnergy() != -1);
        averageEnergyVBox.setManaged(worldMap.getAverageEnergy() != -1);
        averageDaysOfLivingLabel.setText("Srednia dlugosc zycia dla wszystkich martwych zwierzat: " + worldMap.getAverageLifeSpan());
        averageDaysOfLivingVBox.setVisible(worldMap.getAverageLifeSpan() != -1);
        averageDaysOfLivingVBox.setManaged(worldMap.getAverageLifeSpan() != -1);
        averageChildrenNumberLabel.setText("Srednia liczba dzieci dla zyjacych zwierzat: " + worldMap.getAverageNumberOfChildren());
        averageChildrenNumberVBox.setVisible(worldMap.getAverageNumberOfChildren() != -1);
        averageChildrenNumberVBox.setManaged(worldMap.getAverageNumberOfChildren() != -1);
        genotype0.setText("Genotyp 0: " + worldMap.getNumberOfEachGenotype()[0]);
        genotype1.setText("Genotyp 1: " + worldMap.getNumberOfEachGenotype()[1]);
        genotype2.setText("Genotyp 2: " + worldMap.getNumberOfEachGenotype()[2]);
        genotype3.setText("Genotyp 3: " + worldMap.getNumberOfEachGenotype()[3]);
        genotype4.setText("Genotyp 4: " + worldMap.getNumberOfEachGenotype()[4]);
        genotype5.setText("Genotyp 5: " + worldMap.getNumberOfEachGenotype()[5]);
        genotype6.setText("Genotyp 6: " + worldMap.getNumberOfEachGenotype()[6]);
        genotype7.setText("Genotyp 7: " + worldMap.getNumberOfEachGenotype()[7]);

    }

    private Label createGridItem(String content, Vector2d position){
        Label item = new Label(String.valueOf(content));
        item.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        GridPane.setHalignment(item, HPos.CENTER);
        GridPane.setFillHeight(item, true);
        GridPane.setFillWidth(item, true);
        item.setAlignment(Pos.CENTER);
        if (position != null && worldMap.isWithinGoodHarvestArea(position)){
            item.setStyle("-fx-background-color: darkgreen; -fx-text-fill: black;");
        }
        else if (position != null){
            item.setStyle("-fx-text-fill: black; -fx-border-color: grey;");
        }
        else {
            item.setStyle("-fx-background-color: darkgrey; -fx-border-color: grey;");
        }
        return item;
    }

    private Label createPreferredGrassField(){
        Label item = new Label();
        item.setStyle("-fx-background-color: green; -fx-border-color: grey;");
        item.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        GridPane.setHalignment(item, HPos.CENTER);
        GridPane.setFillHeight(item, true);
        GridPane.setFillWidth(item, true);
        return item;
    }

    private Button createAnimalButton(String content, List<Animal> listOfAnimals) {
        Button button = new Button(content);
        button.setMinSize(CELL_WIDTH, CELL_HEIGHT);
        button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        Vector2d position = listOfAnimals.getFirst().getPosition();
        popularGeneAnimals = worldMap.getPopularGenotypeAnimals();
        boolean isAnyAnimalMarked = false;
        boolean foundPopularAnimal = false;
        for (Animal animal : listOfAnimals){
            if (markedAnimal != null && markedAnimal.getId() == animal.getId()){
                isAnyAnimalMarked = true;
            }
            if (popularGeneAnimals.contains(animal)){
                foundPopularAnimal = true;
            }
        }
        if (isAnyAnimalMarked && foundPopularAnimal){
            button.setStyle("-fx-background-color: #ff0000; -fx-text-fill: blue; -fx-border-color: grey;");
        }
        else if (isAnyAnimalMarked) {
            button.setStyle("-fx-background-color: #ff0000; -fx-text-fill: black; -fx-border-color: grey;");
        }
        else if (foundPopularAnimal && worldMap.isWithinGoodHarvestArea(position)) {
            button.setStyle("-fx-background-color: forestgreen; -fx-text-fill: blue; -fx-border-color: grey;");
        }
        else if (foundPopularAnimal) {
            button.setStyle("-fx-background-color: lightgrey; -fx-text-fill: blue; -fx-border-color: grey;");
        }
        else if (worldMap.isWithinGoodHarvestArea(position)) {
            button.setStyle("-fx-background-color: forestgreen; -fx-text-fill: black; -fx-border-color: grey;");
        }
        else {
            button.setStyle("-fx-background-color: lightgrey; -fx-text-fill: black; -fx-border-color: grey;");
        }
        button.setOnAction(event -> {
            Platform.runLater(() -> {
                try {
                    handleButtonClick(button, content, listOfAnimals.getFirst());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        });
        buttonMap.put(listOfAnimals.get(0).getPosition(), button);

        return button;
    }

    private void handleButtonClick(Button button, String content, Animal animal) throws IOException {
        System.out.println("Clicked");
        if (simulation.isStopped()){
            if (content.equals("A")){
                //handleNewWindow(animal.getPosition());
            }
            else {
                if (markedAnimal == animal){
                    setMarkedAnimal(null);
                }
                else {
                    setMarkedAnimal(animal);
                }
            }
        }
    }

    public void drawMap(WorldMap worldMap) {
        buttonMap.clear();
        clearGrid();

        int gridWidth = constants.getMAP_WIDTH() + 2;
        int gridHeight = constants.getMAP_HEIGHT() + 2;
        mapGrid.add(createGridItem("y\\x", null),0,0);
        mapGrid.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));
        mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
        int leftStartingPoint = 0;
        for(int i = 1; i < gridWidth; i++){
            mapGrid.add(createGridItem(String.valueOf(leftStartingPoint), null),i,0);
            mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
            leftStartingPoint++;
        }
        int upperStartingPoint = constants.getMAP_HEIGHT();
        for(int i = 1; i < gridHeight; i++){
            mapGrid.add(createGridItem(String.valueOf(upperStartingPoint), null),0,i);
            mapGrid.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));
            upperStartingPoint--;
        }


        for(int i = 0; i<= constants.getMAP_HEIGHT(); i++){
            for(int j = 0; j <= constants.getMAP_WIDTH(); j++){
                Vector2d vec = new Vector2d(j,i);
                if(worldMap.objectAt(vec) != null && worldMap.objectAt(vec).equals("*")){
                    mapGrid.add(createGridItem(worldMap.objectAt(vec).toString(), vec),j+1,gridHeight - i - 1);
                }
                else if (worldMap.objectAt(vec) != null) {
                    Button button = createAnimalButton(worldMap.objectAt(vec).toString(), worldMap.getAnimalPositions().get(vec));
                    mapGrid.add(button, j + 1, gridHeight - i - 1);
                }
                else if (worldMap.isWithinGoodHarvestArea(vec)){
                    mapGrid.add(createPreferredGrassField(),j+1,gridHeight - i - 1);
                }
            }
        }
    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().getFirst());
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    private SimulationEngine getSimulationEngine(Simulation simulation) {
        worldMap.addObserver(this);
        worldMap.addObserver(new ConsoleMapDisplay());

        return new SimulationEngine(List.of(simulation));

    }

    public void exitApplication(WindowEvent event) {
        simulation.end();
    }

    public void handleNewWindow(Vector2d position) throws IOException {
        AnimalsList animalsList = new AnimalsList();
        SimulationApp app = new SimulationApp();
        Stage stage = app.startAnimalsList(animalsList);
        animalsList.setUp(worldMap.getAnimalPositions().get(position), this, markedAnimal);
    }

    private void setUpWriter() {
        String path = (saveFilePath.getText());
        if(!path.endsWith(".csv")) {
            new Alert(Alert.AlertType.ERROR, "Prosze podac nazwe pliku z rozszerzeniem .csv").show();
            return;
        }
        new CSVWriter(path);
    }

    public void setMarkedAnimal(Animal markedAnimal) {
        if (this.markedAnimal != null){
            Button oldButton = buttonMap.get(this.markedAnimal.getPosition());
            boolean inside = worldMap.isWithinGoodHarvestArea(this.markedAnimal.getPosition());
            if (popularGeneAnimals.contains(this.markedAnimal) && inside){
                oldButton.setStyle("-fx-background-color: forestgreen; -fx-text-fill: blue; -fx-border-color: grey;");
            }
            else if (popularGeneAnimals.contains(this.markedAnimal)){
                oldButton.setStyle("-fx-background-color: lightgrey; -fx-text-fill: blue; -fx-border-color: grey;");
            }
            else if (inside){
                oldButton.setStyle("-fx-background-color: forestgreen; -fx-text-fill: black; -fx-border-color: grey;");
            }
            else {
                oldButton.setStyle("-fx-background-color: lightgrey; -fx-text-fill: black; -fx-border-color: grey;");
            }
        }
        this.markedAnimal = markedAnimal;
        if (this.markedAnimal != null) {
            Button newButton = buttonMap.get(this.markedAnimal.getPosition());
            if (popularGeneAnimals.contains(this.markedAnimal)){
                newButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: blue; -fx-border-color: grey;");
            }
            else {
                newButton.setStyle("-fx-background-color: #ff0000; -fx-text-fill: black; -fx-border-color: grey;");
            }
        }
        if (this.markedAnimal == null){
            worldMap.deleteMark();
        }
        else {
            worldMap.addMark(this.markedAnimal);
        }
    }


}