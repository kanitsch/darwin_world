package agh.ics.oop.model.util;

import agh.ics.oop.model.creatures.Grass;
import agh.ics.oop.model.creatures.LargeGrass;
import agh.ics.oop.model.info.Constants;
import agh.ics.oop.model.info.ConstantsList;

import java.util.*;

public class RandomPositionGenerator implements PositionGenerator {
    private final Random random = new Random();
    private final int mapHeight;
    private final int mapWidth;
    private final int numberOfPlants;
    private final List<Vector2d> positions;
    private final Map<Vector2d, Grass> grass;

    public RandomPositionGenerator(int simulationId, Map<Vector2d, Grass> grass, int numberOfPlants) {
        Constants constants = ConstantsList.getConstants(simulationId);
        this.mapHeight = constants.getMAP_HEIGHT();
        this.mapWidth = constants.getMAP_WIDTH();
        this.numberOfPlants=numberOfPlants;
        this.grass=grass;
        this.positions=new ArrayList<>();
    }

    @Override
    public void initializePositions() {
        for (int x = 0; x <= mapWidth; x++) {
            for (int y = 0; y <= mapHeight; y++) {
                Vector2d position = new Vector2d(x, y);
                if (!grass.containsKey(position)) {
                    positions.add(position);
                }
            }

        }
    }
    @Override
    public List<Vector2d> generatePositions() {
            List<Vector2d> generatedPositions = new ArrayList<>();
            initializePositions();
            for (int i=0; i<numberOfPlants; i++){
                generatedPositions.add(getRandomPosition(positions));
        }
        return generatedPositions;
    }

        private Vector2d getRandomPosition(List<Vector2d> positions) {
                int size = positions.size();
                int idx = random.nextInt(size);
                return positions.remove(idx);
            }


    @Override
    public Iterator<Vector2d> iterator() {
        return new PositionIterator(generatePositions());
    }
}


