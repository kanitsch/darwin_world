package agh.ics.oop.model.maps;

import agh.ics.oop.model.ChangeListener;
import agh.ics.oop.model.creatures.Animal;
import agh.ics.oop.model.creatures.Grass;
import agh.ics.oop.model.creatures.LargeGrass;
import agh.ics.oop.model.creatures.WorldElement;
import agh.ics.oop.model.info.Constants;
import agh.ics.oop.model.info.ConstantsList;
import agh.ics.oop.model.util.*;

import java.util.*;

import static java.lang.Math.min;
import static java.lang.Math.sqrt;

public class WorldMap {
    private final Map<Vector2d, List<Animal>> animals = new HashMap<>();
    private final Map<Vector2d, Grass> grass;
    protected final List<ChangeListener> observers = new ArrayList<>();

    private final int mapWidth;
    private final int mapHeight;
    private final int simulationId;
    private int totalDeadAnimals = 0;
    private int totalLifeSpan = 0;
    private final int goodHarvestAreaWidth;
    private final Vector2d goodHarvestBottomLeft;
    private int largeGrassCount = 0;
    private final int[] numberOfEachGenotype = new int[8];
    private final HashSet<Animal>[] commonGenotypeAnimals = new HashSet[8];
    private Animal markedAnimal;
    private final HashSet<UUID> descendants = new HashSet<>();
    private final Map<UUID, GraphVertex> familyTree = new HashMap<>();
    private CSVWriter csvWriter;


    public WorldMap(int simulationId) {
        this.grass = new HashMap<>();
        Constants constants = ConstantsList.getConstants(simulationId);
        this.simulationId = simulationId;
        int numberOfPlants= constants.getNUMBER_OF_PLANTS();
        this.mapWidth= constants.getMAP_WIDTH();
        this.mapHeight= constants.getMAP_HEIGHT();
        boolean goodHarvest = constants.isGOOD_HARVEST();
        this.goodHarvestAreaWidth = min(min((int) sqrt(0.2*mapHeight*mapWidth),mapWidth),mapHeight);

        for (int i = 0; i < 8; i++) {
            this.commonGenotypeAnimals[i] = new HashSet<>();
        }

        Random random = new Random();
        int xStart = random.nextInt(mapWidth - goodHarvestAreaWidth + 1);
        int yStart = random.nextInt(mapHeight - goodHarvestAreaWidth + 1);
        this.goodHarvestBottomLeft = new Vector2d(xStart, yStart);

        PositionGenerator positionGenerator;
        if (!goodHarvest){
            positionGenerator = new EquatorPreferredGenerator(simulationId,grass,numberOfPlants);
        }
        else {
            positionGenerator = new RandomPositionGenerator(simulationId, grass, numberOfPlants);
        }
        for(Vector2d grassPosition : positionGenerator) {
            grass.put(grassPosition, new Grass(grassPosition));
        }
    }

    public String objectAt(Vector2d position) {
        if (animals.get(position) != null) {
            if (animals.get(position).size() == 1) return animals.get(position).getFirst().toString();
            else return "A";
        }
        if (grass.containsKey(position)) return grass.get(position).toString();
        return null;
    }


    public void place(Animal animal) {
        if (animals.containsKey(animal.getPosition())) {
            animals.get(animal.getPosition()).add(animal);
        }
        else {
            List<Animal> animalList = new ArrayList<>();
            animalList.add(animal);
            animals.put(animal.getPosition(), animalList);
        }
        addToGenomeStats(animal);
    }

    private boolean placeLargeGrass(Vector2d bottomLeftPosition, RandomPositionGenerator positionGenerator) {
        for (int dx = 0; dx <= 1; dx++) {
            for (int dy = 0; dy <= 1; dy++) {
                if (grass.containsKey(new Vector2d(bottomLeftPosition.x + dx, bottomLeftPosition.y + dy))) {
                    return false;
                }
                if (positionGenerator.contains(new Vector2d(bottomLeftPosition.x + dx, bottomLeftPosition.y + dy))) {
                    return false;

                }
            }
        }
        for (int dx = 0; dx <= 1; dx++) {
            for (int dy = 0; dy <= 1; dy++) {
                Vector2d position = bottomLeftPosition.add(new Vector2d(dx, dy));
                Grass large=new LargeGrass(bottomLeftPosition);
                grass.put(position, large);
            }
        }
        return true;
    }


    private void move(Animal animal) {
        Vector2d oldPosition = animal.getPosition();
        animal.move();
        Vector2d newPosition = animal.getPosition();

        if (animals.containsKey(oldPosition)) {
            List<Animal> oldList = animals.get(oldPosition);
            oldList.remove(animal);
            if (oldList.isEmpty()) {
                animals.remove(oldPosition);
            }
        }

        animals.putIfAbsent(newPosition, new ArrayList<Animal>());
        animals.get(newPosition).add(animal);
    }

    public boolean isWithinGoodHarvestArea(Vector2d grassPosition) {
        return grassPosition.getX()>=goodHarvestBottomLeft.getX() && grassPosition.getY()>=goodHarvestBottomLeft.getY() && grassPosition.getX()<goodHarvestBottomLeft.getX()+goodHarvestAreaWidth-1 && grassPosition.getY()<goodHarvestBottomLeft.getY()+goodHarvestAreaWidth-1;
    }


    public void growGrass(){
        Constants constants = ConstantsList.getConstants(simulationId);
        int plantsPerDay = constants.getPLANTS_PER_DAY();
        boolean goodHarvest = constants.isGOOD_HARVEST();
        PositionGenerator positionGenerator;
        if (!goodHarvest){
            positionGenerator = new EquatorPreferredGenerator(simulationId,grass,plantsPerDay);
        }
        else {
            positionGenerator = new RandomPositionGenerator(simulationId, grass, plantsPerDay);
        }

        Random random = new Random();

        for (Vector2d grassPosition : positionGenerator) {

            if (goodHarvest && random.nextDouble()<0.5 && isWithinGoodHarvestArea(grassPosition)) {
                    if(placeLargeGrass(grassPosition,(RandomPositionGenerator)positionGenerator)) {
                        this.largeGrassCount++;
                    }
                    else {
                        grass.put(grassPosition, new Grass(grassPosition));
                    }
            }
            else {
            grass.put(grassPosition, new Grass(grassPosition));
            }
        }
    }

    public void removeDeadAnimals() {
        for (Vector2d position : animals.keySet()) {
            Iterator<Animal> iterator = animals.get(position).iterator();
            while (iterator.hasNext()) {
                Animal animal = iterator.next();
                if (animal.getEnergy() <= 0) {
                    iterator.remove();
                    totalDeadAnimals++;
                    totalLifeSpan+=animal.getAge();
                    removeFromGenomeStats(animal);
                } else {
                    animal.incrementAge();
                }
            }
        }
    }

    private void removeGrass(Grass plant) {
        if (plant instanceof LargeGrass) {
            for (int x=plant.getPosition().getX(); x<plant.getPosition().getX()+2;x++){
                for (int y=plant.getPosition().getY(); y<plant.getPosition().getY()+2; y++){
                    grass.remove(new Vector2d(x, y));
                }
            }
            this.largeGrassCount--;
        }
        else {
            grass.remove(plant.getPosition());
        }
    }

    public void animalsBreed() {
        for (Vector2d position : animals.keySet()) {
            for (int i=0; i<animals.get(position).size()/2; i++) {
                Animal offspring = animals.get(position).get(2*i).breed(animals.get(position).get(2*i+1));
                this.place(offspring);
            }
        }
    }

    public void animalsEat(){
        for (Vector2d position: animals.keySet()){
            animals.get(position).sort(Comparator.comparing(Animal::getEnergy).reversed().thenComparing(Animal::getAge).reversed().thenComparing(Animal::getChildrenNumber).reversed());
            if (grass.containsKey(position)){
                if (grass.get(position) instanceof LargeGrass){
                    Vector2d bottomLeft=grass.get(position).getPosition();
                    List<Animal> candidates = new ArrayList<>();
                    for (int x=bottomLeft.getX(); x<bottomLeft.getX()+2; x++){
                        for (int y=bottomLeft.getY(); y<bottomLeft.getY()+2; y++){
                            if (animals.containsKey(new Vector2d(x, y))) {
                                candidates.addAll(animals.get(new Vector2d(x, y)));
                            }
                        }
                    }
                    if (!candidates.isEmpty()) {
                        candidates.sort(Comparator
                                .comparing(Animal::getEnergy).reversed()
                                .thenComparing(Animal::getAge).reversed()
                                .thenComparing(Animal::getChildrenNumber).reversed());

                    }
                    candidates.get(0).consume(true);
                    //trzeba to uwzględnić w animalu, że roślina ma więcej energii
                }
                else {
                    animals.get(position).get(0).consume(false);
                }
                this.removeGrass(grass.get(position));
            }
        }
    }

    public void animalsMove() {
        List<Animal> allAnimals = new ArrayList<>();
        for (List<Animal> animalList : animals.values()) {
            if (!animalList.isEmpty()) {
                allAnimals.addAll(animalList);
            }
        }
        if (!allAnimals.isEmpty()) {
            for (Animal animal : allAnimals) {
                this.move(animal);
            }
        }
    }

    public int getTotalAnimals() {
        return animals.values().stream().mapToInt(List::size).sum();
    }

    public double getAverageEnergy(){
        int totalEnergy = 0;
        int animalCount = 0;
        for (List<Animal> animalList : animals.values()) {
            for (Animal animal : animalList) {
                totalEnergy += animal.getEnergy();
                animalCount++;
            }
        }
        if (animalCount>0){
            return (double) totalEnergy/animalCount;
        }
        return 0.0;
    }

    public double getAverageLifeSpan(){
        if (totalDeadAnimals>0) {
            return (double) totalLifeSpan / totalDeadAnimals;
        }
        return 0.0;
    }

    public double getAverageNumberOfChildren(){
        int totalChildren = 0;
        int animalCount = 0;
        for (List<Animal> animalList : animals.values()) {
            for (Animal animal : animalList) {
                totalChildren += animal.getChildrenNumber();
                animalCount++;
            }
        }
        if (animalCount>0){
            return (double) totalChildren/animalCount;
        }
        return 0.0;
    }

    public int getTotalPlants() {
        return grass.size()-3*largeGrassCount;
    }

    @Override
    public String toString() {
        MapVisualizer map = new MapVisualizer(this);
        return map.draw(new Vector2d(0, 0), new Vector2d(mapWidth, mapHeight));
    }

    public int getSimulationId() {
        return simulationId;
    }

    public Map<Vector2d, List<Animal>> getAnimalPositions() {
        return animals;
    }

    public void addObserver(ChangeListener observer) {
        observers.add(observer);
    }

    public void removeObserver(ChangeListener observer) {
        observers.remove(observer);
    }

    public void atMapChanged(String str) {
        for(ChangeListener observer : observers) {
            observer.mapChanged(this, str);
        }
    }

    public Map<Vector2d, Grass> getGrass () {
        return grass;
    }

    private void addToGenomeStats (Animal animal){
        int[] genes = animal.getGenome().getGeneList();
        for (int gene : genes){
            // adding genes of this animal to statistics
            numberOfEachGenotype[gene] +=1;
            // assigning animal to its genotypes statistics
            commonGenotypeAnimals[gene].add(animal); // if it's already there, no effects
        }
    }

    private void removeFromGenomeStats (Animal animal){
        int[] genes = animal.getGenome().getGeneList();
        for (int gene : genes){
            // removing genes of this animal from statistics
            numberOfEachGenotype[gene] -=1;
            // the dead animal is no longer counted
            commonGenotypeAnimals[gene].remove(animal); // if it's not there, no effects
        }
    }

    public void addMark (Animal markedAnimal){
        // if previously marked animal is still marked, unmark it
        if (!(markedAnimal == null)){
            deleteMark();
        }

        this.markedAnimal = markedAnimal;
        GraphVertex markedAnimalVertex = familyTree.get(markedAnimal.getId());
        DFS dfs = new DFS();
        List<GraphVertex> descendantsList = dfs.getDescendantsList(markedAnimalVertex);
        for (GraphVertex vertex : descendantsList){
            descendants.add(vertex.getId());
        }
    }

    public void deleteMark(){
        // clear stats for marked animal
        this.markedAnimal = null;
        descendants.clear();
        //daysOfLiving = 0;
        //dayOfDeath = 0;
    }

    public HashSet<Animal> getPopularGenotypeAnimals(){

        int maxValue = numberOfEachGenotype[0];
        List<Integer> indicesWithMaxValue = new ArrayList<>(List.of(0));
        for (int i=1; i<8; i++){
            int nextSize = numberOfEachGenotype[i];
            if (nextSize > maxValue){
                maxValue = nextSize;
                indicesWithMaxValue.clear();
                indicesWithMaxValue.add(i);
            }
            else if (nextSize == maxValue){
                indicesWithMaxValue.add(i);
            }
        }
        HashSet<Animal> popularGenotypeAnimals = new HashSet<>();
        for (int genotype : indicesWithMaxValue){
            popularGenotypeAnimals.addAll(commonGenotypeAnimals[genotype]);
        }
        return popularGenotypeAnimals;
    }

    public int[] getNumberOfEachGenotype() {
        return numberOfEachGenotype;
    }

    private String[] getStatsForCSV() {
        String[] data = new String[6];
        data[0] = String.valueOf(getTotalAnimals());
        data[1] = String.valueOf(totalLifeSpan);
        //data[2] = String.valueOf(numberOfEmptyFields);
        data[3] = String.valueOf(getAverageEnergy());
        data[4] = String.valueOf(getAverageLifeSpan());
        data[5] = String.valueOf(getAverageNumberOfChildren() );
        return data;
    }
}

