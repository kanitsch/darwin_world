package agh.ics.oop.model.info;

import java.util.HashMap;

public class ConstantsList {

    private static final HashMap<Integer, Constants> constantsList = new HashMap<>();

    public static Constants getConstants(int simulationId) {
        return constantsList.get(simulationId);
    }

    public static void addToConstantsList(int simulationId, Constants constantsObj) {
        constantsList.put(simulationId, constantsObj);
    }
}