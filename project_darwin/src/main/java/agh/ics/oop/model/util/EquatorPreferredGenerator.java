package agh.ics.oop.model.util;

import agh.ics.oop.model.creatures.Grass;
import agh.ics.oop.model.info.Constants;
import agh.ics.oop.model.info.ConstantsList;

import java.util.*;

public class EquatorPreferredGenerator implements PositionGenerator {
    private final Constants constants;
    private final List<Vector2d> preferredPositions;
    private final List<Vector2d> nonPreferredPositions;
    private final Random random = new Random();
    private final int mapHeight;
    private final int mapWidth;
    private final int numberOfPlants;
    private final Map<Vector2d, Grass> grass;


    public EquatorPreferredGenerator(int simulationId, Map<Vector2d, Grass> grass, int numberOfPlants) {
        this.preferredPositions = new ArrayList<>();
        this.nonPreferredPositions = new ArrayList<>();
        this.constants= ConstantsList.getConstants(simulationId);
        this.mapHeight = constants.getMAP_HEIGHT();
        this.mapWidth = constants.getMAP_WIDTH();
        this.numberOfPlants=numberOfPlants;
        this.grass=grass;


    }
    public void initializePositions(){
        int equatorStart= (int) (constants.getMAP_HEIGHT()*0.4);
        int equatorEnd= (int) (constants.getMAP_HEIGHT()*0.6);

        for (int x=0; x<=mapWidth; x++){
            for (int y=0; y<=mapHeight; y++){
                Vector2d position = new Vector2d(x,y);
                if (!grass.containsKey(position)) {
                    if (y >= equatorStart && y <= equatorEnd) {
                        preferredPositions.add(position);
                    } else {
                        nonPreferredPositions.add(position);
                    }
                }
            }
        }
    }

    public List<Vector2d> generatePositions() {
            List<Vector2d> generatedPositions = new ArrayList<>();
            initializePositions();

            for (int i = 0; i < numberOfPlants; i++) {
                if (random.nextDouble() < 0.8 && preferredPositions.size() > 0) {
                    generatedPositions.add(getRandomPosition(preferredPositions));
                } else {
                    if (nonPreferredPositions.size() > 0) {
                        generatedPositions.add(getRandomPosition(nonPreferredPositions));
                    }
                }
            }
            return generatedPositions;
        }

    @Override
    public boolean contains(Vector2d vector2d) {
        return false;
        // ta metoda jest tu nie potrzebna, ale musi byc zachowany interfejs positiongenerator
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
