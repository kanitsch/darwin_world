package agh.ics.oop.model.creatures;

import agh.ics.oop.model.util.Vector2d;

import java.util.ArrayList;
import java.util.List;

public class Grass implements WorldElement {
    Vector2d position;

    public Grass(Vector2d position) {
        this.position = position;

    }

    public Vector2d getPosition() {
        return position;
    }


    public String toString() {
        return "*";
    }
}
