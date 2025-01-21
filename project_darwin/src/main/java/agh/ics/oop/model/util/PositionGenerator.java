package agh.ics.oop.model.util;

import java.util.List;
import java.util.Random;

public interface PositionGenerator extends Iterable<Vector2d>{
    public void initializePositions();
    public List<Vector2d> generatePositions();
}
