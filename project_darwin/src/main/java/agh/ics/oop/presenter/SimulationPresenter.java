package agh.ics.oop.presenter;

import agh.ics.oop.model.creatures.Animal;
import agh.ics.oop.model.info.Constants;
import agh.ics.oop.model.util.Vector2d;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextFlow;

import java.util.*;

public class SimulationPresenter extends BasePresenter {
    @FXML
    private Button startStopGame;

    // General Statistics fields
    @FXML
    private Label generalStatisticsDays;
    @FXML
    private Label generalStatisticsAnimals;
    @FXML
    private Label generalStatisticsPlants;
    @FXML
    private Label generalStatisticsFreeFields;
    @FXML
    private Label generalStatisticsBestGenotypes1;
    @FXML
    private Label generalStatisticsBestGenotypes2;
    @FXML
    private Label generalStatisticsBestGenotypes3;
    @FXML
    private Label generalStatisticsAverageEnergy;
    @FXML
    private Label generalStatisticsAverageAge;
    @FXML
    private Label generalStatisticsAverageChildren;

    // Animal Statistics fields
    @FXML
    private TextFlow animalStatisticsGenotype;
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

    // Additional info display
    @FXML
    private Button showAnimalsWithMostPopularGenotypes;
    @FXML
    private Button showPreferredFields;

    private Constants constants;
    private final Map<Vector2d, Pane> fields = new HashMap<>();
    private Animal selectedAnimal = null;
    private List<Animal> animalsWithMostPopularGenotypes = new ArrayList<>();
    private Set<Vector2d> mostPreferredPositions = new HashSet<>();

    private boolean shouldExportStatistics;
    private boolean showExportStatisticsAlert = true;

    @FXML
    private GridPane generalStatisticsPane;

    @FXML
    private GridPane boardPane;

    private boolean isShowedAnimalsWithMostPopularGenotypes = false;
    private boolean isShowedPreferredFields = false;

}
