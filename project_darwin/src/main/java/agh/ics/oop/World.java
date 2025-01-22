package agh.ics.oop;

import agh.ics.oop.model.info.Constants;
import agh.ics.oop.model.info.ConstantsList;
import javafx.application.Application;


public class World {
    public static void main(String[] args) {

        System.out.print("System zaczal dzialac");

        Application.launch(SimulationApp.class);

        System.out.println("System skonczyl dzialanie");
        System.exit(0);


    }
    public static void setUpConstants(int simulationId) {
        int NUMBER_OF_GENES = 10;
        int STARTING_ANIMAL_ENERGY = 15;
        int ENERGY_FROM_PLANT = 20;
        int MAP_WIDTH = 10;
        int MAP_HEIGHT = 10;
        boolean GOOD_HARVEST = false;
        boolean COMPLETE_RANDOM = false;
        int NUMBER_OF_ANIMALS = 15;
        int NUMBER_OF_PLANTS = 50;
        int GENOME_LENGTH = 10;
        int MIN_MUTATIONS = 0;
        int MAX_MUTATIONS = 3;
        int PLANTS_PER_DAY = 25;
        int MINIMAL_BREEDING_ENERGY = 30;
        int ENERGY_LOST_FOR_REPRODUCTION = 25;
        int ENERGY_LOST_PER_DAY = 1;

        Constants mockConsts = new Constants(
                NUMBER_OF_GENES,
                STARTING_ANIMAL_ENERGY,
                ENERGY_FROM_PLANT,
                MAP_WIDTH,
                MAP_HEIGHT,
                GOOD_HARVEST,
                COMPLETE_RANDOM,
                NUMBER_OF_ANIMALS,
                NUMBER_OF_PLANTS,
//                GENOME_LENGTH,
                MIN_MUTATIONS,
                MAX_MUTATIONS,
                PLANTS_PER_DAY,
                MINIMAL_BREEDING_ENERGY,
                ENERGY_LOST_FOR_REPRODUCTION,
                ENERGY_LOST_PER_DAY
        );

        ConstantsList.addToConstantsList(simulationId, mockConsts);
    }

}