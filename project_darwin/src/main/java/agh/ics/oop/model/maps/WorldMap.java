package agh.ics.oop.model.maps;

import agh.ics.oop.model.creatures.Animal;
import agh.ics.oop.model.creatures.Grass;
import agh.ics.oop.model.creatures.LargeGrass;
import agh.ics.oop.model.creatures.WorldElement;
import agh.ics.oop.model.info.Constants;
import agh.ics.oop.model.info.ConstantsList;
import agh.ics.oop.model.util.EquatorPreferredGenerator;
import agh.ics.oop.model.util.PositionGenerator;
import agh.ics.oop.model.util.RandomPositionGenerator;
import agh.ics.oop.model.util.Vector2d;

import java.util.*;

public class WorldMap {
    private final Map<Vector2d, List<Animal>> animals = new HashMap<>();
    private final Map<Vector2d, Grass> grass;
    private final int mapWidth;
    private final int mapHeight;
    private final int simulationId;
    private int totalDeadAnimals = 0;
    private int totalLifeSpan = 0;




    public WorldMap(int simulationId) {
        this.grass = new HashMap<>();
        Constants constants = ConstantsList.getConstants(simulationId);
        this.simulationId = simulationId;
        int numberOfPlants= constants.getNUMBER_OF_PLANTS();
        this.mapWidth= constants.getMAP_WIDTH();
        this.mapHeight= constants.getMAP_HEIGHT();
        boolean goodHarvest = constants.isGOOD_HARVEST();
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
        // nie generuje pozycji dla dorodnych plonów
    }

    public WorldElement objectAt(Vector2d position) {
        if (animals.get(position) != null) {
            if (!animals.get(position).isEmpty()) return animals.get(position).iterator().next();
        }

        return grass.get(position);
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
        for(Vector2d grassPosition : positionGenerator) {
            grass.put(grassPosition, new Grass(grassPosition));
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
                } else {
                    animal.incrementAge();
                }
            }

        }
    }

    private void removeGrass(Grass plant) {
        grass.remove(plant.getPosition());
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
                animals.get(position).get(0).consume();
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
        return grass.size();
    }


    @Override
    public String toString() {
        MapVisualizer map = new MapVisualizer(this);
        return map.draw(new Vector2d(0, 0), new Vector2d(mapWidth, mapHeight));

    }
}

