package agh.ics.oop.model;

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

    public MapDirection next(){
        return switch(this){
            case N -> NE;
            case NE -> E;
            case E -> SE;
            case SE -> S;
            case S -> SW;
            case SW -> W;
            case W -> NW;
            case NW -> N;
        };
    }

}
