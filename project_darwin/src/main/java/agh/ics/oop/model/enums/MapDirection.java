package agh.ics.oop.model.enums;

import agh.ics.oop.model.util.Vector2d;

public enum MapDirection {
    N,
    NE,
    E,
    W,
    S,
    SE,
    SW,
    NW;

    public String toString(){
        return switch(this) {
            case N -> "N";
            case S -> "S";
            case E -> "E";
            case W -> "W";
            case NW -> "NW";
            case NE -> "NE";
            case SE -> "SE";
            case SW -> "SW";
        };
    }

    public MapDirection rotate(int rotations) {
        int nextIndex = (this.ordinal() + rotations) % values().length;
        return values()[nextIndex];
    }

    public Vector2d toUnitVector() {
        return switch (this) {
            case N -> new Vector2d(0, 1);
            case NE -> new Vector2d(1, 1);
            case E -> new Vector2d(1, 0);
            case SE -> new Vector2d(1, -1);
            case S -> new Vector2d(0, -1);
            case SW -> new Vector2d(-1, -1);
            case W -> new Vector2d(-1, 0);
            case NW -> new Vector2d(-1, 1);
        };
    }

}
