package agh.ics.oop.model.creatures;

import agh.ics.oop.model.util.Vector2d;

import java.util.ArrayList;
import java.util.List;

public class LargeGrass extends Grass {
        private final Vector2d position;

        public LargeGrass(Vector2d position) {
            super(position);
            this.position = position;
        }

        public List<Vector2d> getCoveredPositions() {
            List<Vector2d> coveredPositions = new ArrayList<>();
            for (int x = 0; x < 2; x++) {
                for (int y = 0; y < 2; y++) {
                    coveredPositions.add(new Vector2d(position.getX() + x, position.getY() + y));
                }
            }
            return coveredPositions;
        }

    @Override
    public String toString() {
        return "#";
    }
}
