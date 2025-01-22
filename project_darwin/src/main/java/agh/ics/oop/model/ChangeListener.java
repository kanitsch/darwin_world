package agh.ics.oop.model;

import agh.ics.oop.model.maps.WorldMap;

public interface ChangeListener {
    void mapChanged(WorldMap worldMap, String message);

}