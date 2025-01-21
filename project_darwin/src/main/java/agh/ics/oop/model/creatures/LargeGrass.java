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


    @Override
    public String toString() {
        return "#";
    }
}
