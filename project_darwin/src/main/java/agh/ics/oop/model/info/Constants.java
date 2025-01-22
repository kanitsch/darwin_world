package agh.ics.oop.model.info;

public class Constants {
    private  int NUMBER_OF_GENES;
    private  int STARTING_ANIMAL_ENERGY;
    private  int ENERGY_FROM_PLANT;
    private int MAP_WIDTH;
    private int MAP_HEIGHT;
    private boolean GOOD_HARVEST;
    private boolean COMPLETE_RANDOM;
    private int NUMBER_OF_ANIMALS;
    private int NUMBER_OF_PLANTS;
//    private int GENOME_LENGTH;
    private int MIN_MUTATIONS;
    private int MAX_MUTATIONS;
    private int PLANTS_PER_DAY;
    private int MINIMAL_BREEDING_ENERGY;
    private int ENERGY_LOST_FOR_REPRODUCTION;
    private int ENERGY_LOST_PER_DAY;







    public Constants (
            int NUMBER_OF_GENES,
            int STARTING_ANIMAL_ENERGY,
            int ENERGY_FROM_PLANT,
            int mapWidth,
            int mapHeight,
            boolean goodHarvest,
            boolean completeRandom,
            int numberOfAnimals,
            int numberOfPlants,
//            int genomeLength,
            int minMutations,
            int maxMutations,
            int plantsPerDay,
            int minimalBreedingEnergy,
            int energyLostForReproduction,
            int energyLostPerDay
    ) {
        this.NUMBER_OF_GENES = NUMBER_OF_GENES;
        this.STARTING_ANIMAL_ENERGY = STARTING_ANIMAL_ENERGY;
        this.ENERGY_FROM_PLANT = ENERGY_FROM_PLANT;

        MAP_WIDTH = mapWidth;
        MAP_HEIGHT = mapHeight;
        GOOD_HARVEST = goodHarvest;
        COMPLETE_RANDOM = completeRandom;
        NUMBER_OF_ANIMALS = numberOfAnimals;
        NUMBER_OF_PLANTS = numberOfPlants;
//        GENOME_LENGTH = genomeLength;
        MIN_MUTATIONS = minMutations;
        MAX_MUTATIONS = maxMutations;
        PLANTS_PER_DAY = plantsPerDay;
        ENERGY_LOST_PER_DAY = energyLostPerDay;
        MINIMAL_BREEDING_ENERGY = minimalBreedingEnergy;
        ENERGY_LOST_FOR_REPRODUCTION = energyLostForReproduction;
    }

    //getters
    public int getNUMBER_OF_GENES () {
        return NUMBER_OF_GENES;
    }

    public int getSTARTING_ANIMAL_ENERGY () {
        return STARTING_ANIMAL_ENERGY;
    }

    public int getENERGY_FROM_PLANT () {
        return ENERGY_FROM_PLANT;
    }
    public int getMAP_WIDTH() {
        return MAP_WIDTH;
    }
    public int getMAP_HEIGHT () {
        return MAP_HEIGHT;
    }
    public boolean isGOOD_HARVEST () {
        return GOOD_HARVEST;
    }
    public boolean isCOMPLETE_RANDOM () {
        return COMPLETE_RANDOM;
    }
    public int getNUMBER_OF_ANIMALS () {
        return NUMBER_OF_ANIMALS;
    }
    public int getNUMBER_OF_PLANTS () {
        return NUMBER_OF_PLANTS;
    }
//    public int getGENOME_LENGTH () {
//        return GENOME_LENGTH;
//    }
    public int getMIN_MUTATIONS () {
        return MIN_MUTATIONS;
    }
    public int getENERGY_LOST_FOR_REPRODUCTION () {
        return ENERGY_LOST_FOR_REPRODUCTION;
    }
    public int getMAX_MUTATIONS () {
        return MAX_MUTATIONS;
    }
    public int getPLANTS_PER_DAY () {
        return PLANTS_PER_DAY;
    }
    public int getMINIMAL_BREEDING_ENERGY () {
        return MINIMAL_BREEDING_ENERGY;
    }
    public int getENERGY_LOST_PER_DAY() {
        return ENERGY_LOST_PER_DAY;
    }



    //setters
    public void setNUMBER_OF_GENES (int NUMBER_OF_GENES) {
        this.NUMBER_OF_GENES = NUMBER_OF_GENES;
    }

    public void setSTARTING_ANIMAL_ENERGY (int STARTING_ANIMAL_ENERGY) {
        this.STARTING_ANIMAL_ENERGY = STARTING_ANIMAL_ENERGY;
    }

    public void setENERGY_FROM_PLANT (int ENERGY_FROM_PLANT) {
        this.ENERGY_FROM_PLANT = ENERGY_FROM_PLANT;
    }
}