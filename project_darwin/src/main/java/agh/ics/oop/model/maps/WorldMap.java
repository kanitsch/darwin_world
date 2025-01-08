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

import java.util.HashMap;
import java.util.Map;

public class WorldMap {
    private final Map<Vector2d, Animal> animals = new HashMap<>();
    private final Map<Vector2d, Grass> grass;
    private final Map<Vector2d, LargeGrass> largeGrass = new HashMap<>();

    private final int mapWidth;
    private final int mapHeight;
    private final int simulationId;


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
            positionGenerator = new RandomPositionGenerator(simulationId, grass, largeGrass, numberOfPlants);
        }
        for(Vector2d grassPosition : positionGenerator) {
            grass.put(grassPosition, new Grass(grassPosition));
        }
        // nie generuje pozycji dla dorodnych plonów
    }

    public WorldElement objectAt(Vector2d position) {
        if (animals.get(position) != null) {
            return animals.get(position);
        }

        for (LargeGrass lg : largeGrass.values()) {
            if (lg.getCoveredPositions().contains(position)) {
                return lg;
            }
        }

        return grass.get(position);
    }


    public void place(Animal animal) {
        animals.put(animal.getPosition(), animal);
    }

    public void move(Animal animal) {
        Vector2d oldPosition = animal.getPosition();
        animal.move();
        animals.remove(oldPosition);
        animals.put(animal.getPosition(), animal);
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
            positionGenerator = new RandomPositionGenerator(simulationId, grass, largeGrass, plantsPerDay);
        }
        for(Vector2d grassPosition : positionGenerator) {
            grass.put(grassPosition, new Grass(grassPosition));
        }
    }



    public boolean canMoveTo(Vector2d position) {
        return position.getX() <= mapWidth && position.getY() <= mapHeight
                && position.getX() >= 0 && position.getY() >= 0;
    }

    @Override
    public String toString() {
        MapVisualizer map = new MapVisualizer(this);
        return map.draw(new Vector2d(0, 0), new Vector2d(mapWidth, mapHeight));

    }
}
