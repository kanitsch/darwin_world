
package agh.ics.oop.model.creatures;

import agh.ics.oop.model.enums.MapDirection;
import agh.ics.oop.model.info.Constants;
import agh.ics.oop.model.info.ConstantsList;
import agh.ics.oop.model.util.Vector2d;

import java.util.*;

public class Animal implements WorldElement {
    private final int simulationId;
    private final UUID id;
    private final Constants constants;
    private Vector2d position;
    private MapDirection direction;
    private int energy;
    private int childrenNumber = 0;
    private int eatenPlantsNumber = 0;
    private Genome genome;
    private int age;


    public Animal(Vector2d position, int simulationId, int energy, Genome genome) {
        this.simulationId = simulationId;
        this.id = UUID.randomUUID();
        this.constants = ConstantsList.getConstants(simulationId);
        this.position = position;
        this.direction = MapDirection.values()[(int) (Math.random() * MapDirection.values().length)];
        this.energy = energy;
        this.genome = genome;
        this.age = 0;
    }


    public MapDirection getDirection() {
        return this.direction;
    }

    public Vector2d getPosition() {
        return this.position;
    }

    public int getEnergy() {
        return this.energy;
    }

    public Genome getGenome() {
        return this.genome;
    }

    public UUID getId() {
        return this.id;
    }

    public int getSimulationId() {
        return this.simulationId;
    }

    public int getChildrenNumber() {return this.childrenNumber;}

    public int getEatenPlantsNumber() {return this.eatenPlantsNumber;}

    public int getAge() {return this.age;}

    public void setPosition(Vector2d position) {this.position = position;}
    public void setDirection(MapDirection direction) {this.direction = direction;}

    public String toString() {
        return this.direction.toString();
    }

    public boolean isAt (Vector2d position) {
        return this.position.equals(position);
    }

    public void incrementAge(){
        this.age++;
    }


    public void move() {
        direction = direction.rotate(genome.getCurrentGene());
        if (position.add(direction.toUnitVector()).getY()<=constants.getMAP_HEIGHT() && position.add(direction.toUnitVector()).getY() >=0) {
            position = position.add(direction.toUnitVector());
            if (position.getX()>constants.getMAP_WIDTH()){
                position=new Vector2d(0, position.getY());
            }
            if (position.getX()<0){
                position=new Vector2d(constants.getMAP_WIDTH(), position.getY());
            }
        }
        else {
            setDirection(direction.rotate(4));
        }
        removeEnergy(constants.getENERGY_LOST_PER_DAY());
    }

    public void consume(boolean isLarge) {
        if (isLarge) {
            energy += 4*constants.getENERGY_FROM_PLANT();
            eatenPlantsNumber++;
        }
        else {
            energy += constants.getENERGY_FROM_PLANT();
            eatenPlantsNumber++;
        }
    }

    public void removeEnergy(int energyToRemove) {
        energy -= energyToRemove;
    }

    public static Animal startingAnimal (int simulationId) {
        Constants constants = ConstantsList.getConstants(simulationId);
        Genome genome = Genome.startingAnimalGenome(simulationId);
        Vector2d position = new Vector2d(2,2);
        int energy = constants.getSTARTING_ANIMAL_ENERGY();

        return new Animal(position, simulationId,energy,genome);
    }

    public static Animal offspring (Animal father, Animal mother) {
        Constants constants = ConstantsList.getConstants(father.getSimulationId());
        Genome genome = Genome.offspringAnimalGenome(father, mother);
        int energy = 2 * constants.getENERGY_LOST_FOR_REPRODUCTION();

        return new Animal(father.position,father.getSimulationId(),energy,genome);
    }

    //working on this
    public Animal breed (Animal partner) {
        if (energy < constants.getMINIMAL_BREEDING_ENERGY()
                || partner.energy < constants.getMINIMAL_BREEDING_ENERGY())
            return null;

        Animal offspring = Animal.offspring(partner, this);

        this.removeEnergy(constants.getENERGY_LOST_FOR_REPRODUCTION());
        partner.removeEnergy(constants.getENERGY_LOST_FOR_REPRODUCTION());
        this.childrenNumber++;
        partner.childrenNumber++;

        return offspring;
    }
}
