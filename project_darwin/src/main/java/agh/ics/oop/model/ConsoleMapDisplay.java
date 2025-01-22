package agh.ics.oop.model;

import agh.ics.oop.model.maps.WorldMap;

import java.util.ArrayList;
import java.util.List;

public class ConsoleMapDisplay implements ChangeListener {

    List<String> changeLog = new ArrayList<>();

    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        synchronized (this) {
            changeLog.add(message);
            System.out.println();
            System.out.printf("Map number: #%d%n", worldMap.getSimulationId());
            System.out.println(message);
            System.out.println(worldMap);
            System.out.printf("Total number of changes: %d%n", changeLog.size());
        }

    }

}