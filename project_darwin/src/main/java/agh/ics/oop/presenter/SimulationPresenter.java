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
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class SimulationPresenter implements ChangeListener {

    @FXML
    private GridPane mapGrid;
    @FXML
    private Button resumeButton;
    @FXML
    private Button pauseButton;

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
    private Label mostPopularGenotypeLabel=new Label();
    @FXML

    private Label getNumberOfEmptyFieldsLabel = new Label();

    static final int CELL_WIDTH = 40;
    static final int CELL_HEIGHT = 40;

    @FXML
    private ScrollPane mapScrollPane;

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
    private boolean shouldExportStatistics;
    private int sleepTime;


    @FXML
    private Label animalStatisticsGenotype;
    @FXML
    private Label animalStatisticsEnergy;
    @FXML
    private Label animalStatisticsPlants;
    @FXML
    private Label animalStatisticsChildren;
    @FXML
    private Label animalStatisticsDescendants;
    @FXML
    private Label animalStatisticsLifeDays;
    @FXML
    private Label animalStatisticsDeathDay;


    public void run() throws InterruptedException {
        engine.runAsync();

    }

    public SimulationPresenter() {}

    public void setShouldExportStatistics(boolean shouldExportStatistics) {
        this.shouldExportStatistics = shouldExportStatistics;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public void setUp(int simulationId, SimulationApp app, Stage stage) {
        this.app = app;

        this.simulationId = simulationId;
        this.stage = stage;
        this.constants = ConstantsList.getConstants(simulationId);
        double scaleX =  min(1, (double) 600 / (constants.getMAP_WIDTH() * CELL_WIDTH));
        double scaleY =  min(1, (double) 600 / (constants.getMAP_HEIGHT() * CELL_HEIGHT));
        double scale = min(scaleX, scaleY); // Use the smaller scale to fit both dimensions

        mapGrid.setScaleX(scale);
        mapGrid.setScaleY(scale);


        this.simulation = new Simulation(this.simulationId, sleepTime);
        this.worldMap = simulation.getMap();

        this.engine = getSimulationEngine(simulation);
        Platform.runLater(() -> {
                changeStats(worldMap);
                drawMap(worldMap);
            Platform.runLater(this::scrollToCenter);
        });



        resumeButton.setOnAction((e) -> simulation.resume());
        pauseButton.setOnAction((e) -> simulation.pause());

        stage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::exitApplication);
    }

    private void scrollToCenter() {
        // Set scroll position to 50% for both horizontal and vertical scrollbars
        mapScrollPane.setHvalue(0.5);
        mapScrollPane.setVvalue(0.5);
    }

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        if(simulationId == -1) throw new RuntimeException("Didn't set-up UI presenter before calling mapChanged");
        Platform.runLater(() -> {
            changeStats(worldMap);
            drawMap(worldMap);
        });

        if (shouldExportStatistics) {
                exportCsvStatistics(worldMap);
        };


    }

    public void exportCsvStatistics(WorldMap worldMap) {
        String projectPath = System.getProperty("user.dir");
        String filename = "World_Statistics_" + worldMap.getSimulationId() + ".csv";
        var path = new File( "src/main/resources/statistics/");
        if (!path.exists()) {
            if (!path.mkdirs()) {
                return;
            }
        }
        String filePath = projectPath + "/src/main/resources/statistics/" + filename;


        File csvFile = new File(filePath);

        boolean fileExist = csvFile.exists();

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, true))) {
            if (!fileExist) {
                CSVWriter.setStatisticsHeader(writer);
            }

            CSVWriter.fillStatisticsDay(writer, worldMap, simulation,markedAnimal);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void changeStats (WorldMap worldMap){
        System.out.println("Stats Changed");
        numberOfAnimalsLabel.setText("Number of alive animals: " + worldMap.getTotalAnimals());
        numberOfPlantsLabel.setText("Actual number of plants: " + worldMap.getTotalPlants());
        numberOfEmptyFieldsLabel.setText("Actual number of free fields: " + worldMap.getFreeFields());
        averageEnergyLabel.setText("Average energy: " + worldMap.getAverageEnergy());
        averageEnergyVBox.setVisible(worldMap.getAverageEnergy() != -1);
        averageEnergyVBox.setManaged(worldMap.getAverageEnergy() != -1);
        averageDaysOfLivingLabel.setText("Average life span: " + worldMap.getAverageLifeSpan());
        averageDaysOfLivingVBox.setVisible(worldMap.getAverageLifeSpan() != -1);
        averageDaysOfLivingVBox.setManaged(worldMap.getAverageLifeSpan() != -1);
        averageChildrenNumberLabel.setText("Average number of children: " + worldMap.getAverageNumberOfChildren());
        averageChildrenNumberVBox.setVisible(worldMap.getAverageNumberOfChildren() != -1);
        averageChildrenNumberVBox.setManaged(worldMap.getAverageNumberOfChildren() != -1);
        mostPopularGenotypeLabel.setText("The most popular genotype: "+worldMap.getMostPopularGenotype());

        if (markedAnimal != null) {
            animalStatisticsChildren.setText("Animal Children: " + markedAnimal.getChildrenNumber());
            animalStatisticsDescendants.setText("Animal Descendants: " + markedAnimal.getNumberOfDescendants());
            animalStatisticsEnergy.setText("Animal Energy: " + markedAnimal.getEnergy());
            animalStatisticsDeathDay.setText("Animal Death day: " + markedAnimal.getDateOfDeath() );
            animalStatisticsPlants.setText("Plants Eaten: " + markedAnimal.getEatenPlantsNumber());
            animalStatisticsGenotype.setText("Genotype: " + Arrays.toString(markedAnimal.getGenome().getGeneList()));
            animalStatisticsLifeDays.setText("Age: " + markedAnimal.getAge());

            if (markedAnimal != null && markedAnimal.getDateOfDeath()>=0) {
                setMarkedAnimal(null);
            }
        }
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
            if (markedAnimal == animal){
                setMarkedAnimal(null);
            }
            else {
                setMarkedAnimal(animal);
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
                if(worldMap.objectAt(vec) != null && (worldMap.objectAt(vec).equals("*") ||worldMap.objectAt(vec).equals("#")) ){
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
        //Platform.runLater(this::scrollToCenter);
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
            worldMap.deleteMark();

        }
    }


}