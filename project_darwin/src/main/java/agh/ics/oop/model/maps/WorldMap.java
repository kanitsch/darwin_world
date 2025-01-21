package agh.ics.oop.model.maps;

import agh.ics.oop.model.creatures.Animal;
import agh.ics.oop.model.creatures.Grass;
import agh.ics.oop.model.creatures.WorldElement;
import agh.ics.oop.model.info.Constants;
import agh.ics.oop.model.info.ConstantsList;
import agh.ics.oop.model.util.*;

import java.util.*;

public class WorldMap {
    private int simulationId;
    private Map<Vector2d, List<Animal>> animals = new HashMap<>();
    private final Map<Vector2d, Grass> grass;
    private final int mapWidth;
    private final int mapHeight;


    public WorldMap(int simulationId) {
        this.simulationId = simulationId;
        this.grass = new HashMap<>();
        Constants constants = ConstantsList.getConstants(simulationId);
        int numberOfPlants= constants.getNUMBER_OF_PLANTS();
        this.mapWidth= constants.getMAP_WIDTH();
        this.mapHeight= constants.getMAP_WIDTH();
        boolean goodHarvest = constants.isGOOD_HARVEST();
        PositionGenerator positionGenerator;
        if (!goodHarvest){
            positionGenerator = new EquatorPreferredGenerator(simulationId,grass);
        }
        else {
            positionGenerator = new RandomPositionGenerator(mapWidth, mapHeight, numberOfPlants);
        }
        for(Vector2d grassPosition : positionGenerator) {
            grass.put(grassPosition, new Grass(grassPosition));
        }
        // nie generuje pozycji dla dorodnych plonów
    }

    public WorldElement objectAt(Vector2d position) {
        if (animals.get(position)!=null){
            if (animals.get(position).size() == 1) return animals.get(position).getFirst();
        }
        return grass.get(position);
    }

    public void place(Animal animal) {
        animals.computeIfAbsent(animal.getPosition(), k -> new ArrayList<>()).add(animal);
    }

    public void move(Animal animal) {
        Vector2d oldPosition = animal.getPosition();
        animal.move();
        animals.remove(oldPosition);
        animals.computeIfAbsent(animal.getPosition(), k -> new ArrayList<>()).add(animal);
    }


    @Override
    public String toString() {
        MapVisualizer map=new MapVisualizer(this);
        return map.draw(new Vector2d(0,0), new Vector2d(mapWidth,mapHeight));
    }

    public Map<Vector2d, List<Animal>> GetAnimals () {
        return animals;
    }

    public Map<Vector2d, Grass> getGrass() {
        return grass;
    }


    public int getSimulationId() {
        return simulationId;
    }



}

