package agh.ics.oop.model.info;

public class Constants {
    private  int NUMBER_OF_GENES;
    private  int STARTING_ANIMAL_ENERGY;
    private  int ENERGY_FROM_PLANT;



    public Constants (
      int NUMBER_OF_GENES,
      int STARTING_ANIMAL_ENERGY,
      int ENERGY_FROM_PLANT
    ) {
        this.NUMBER_OF_GENES = NUMBER_OF_GENES;
        this.STARTING_ANIMAL_ENERGY = STARTING_ANIMAL_ENERGY;
        this.ENERGY_FROM_PLANT = ENERGY_FROM_PLANT;

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
