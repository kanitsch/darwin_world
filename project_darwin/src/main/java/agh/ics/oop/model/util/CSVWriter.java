package agh.ics.oop.model.util;



import agh.ics.oop.Simulation;
import agh.ics.oop.World;
import agh.ics.oop.model.creatures.Animal;
import agh.ics.oop.model.maps.WorldMap;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.UUID;

public class CSVWriter {

    public static void setStatisticsHeader(PrintWriter writer) {
        writer.println("SimulationId,Day number,All animals,All plants,Average animals energy,Average animals age,Average animals children,AnimalID,Genotype,Energy,Eaten plants,Children,Descendants,Age,Date of death");
    }

    public static void fillStatisticsDay(PrintWriter writer, WorldMap worldMap, Simulation simulation, Animal animal) {
        writer.println(worldMap.getSimulationId() + "," +
                // General statistics
                simulation.getDay() + "," +
                worldMap.getTotalAnimals() + "," +
                worldMap.getTotalPlants() + "," +
                //worldMap.getFreeFieldsCount() + "," +
                worldMap.getAverageEnergy() + "," +
                worldMap.getAverageLifeSpan() + "," +
                worldMap.getAverageNumberOfChildren() + "," +

                // Animal statistics
                (animal != null ? animal.getId() : "notMarked") + "," +
                (animal != null ? Arrays.toString(animal.getGenome().getGeneList()) : "notMarked") + "," +
                (animal != null ? animal.getEnergy() : "notMarked") + "," +
                (animal != null ? animal.getEatenPlantsNumber() : "notMarked") + "," +
                (animal != null ? animal.getChildrenNumber() : "notMarked") + "," +
                (animal != null ? animal.getChildrenNumber() : "notMarked") + "," +
                (animal != null ? animal.getAge() : "notMarked") + "," +
                (animal != null ? animal.getAge() : "notMarked")
        );
    }

    public static void setConfigurationHeader(PrintWriter writer) {
        writer.println("Property,Value");
    }
}