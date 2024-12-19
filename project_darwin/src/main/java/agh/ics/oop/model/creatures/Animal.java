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
    private int childrenNumber;
    private int eatenPlantsNumber = 0;
    private Genome genome;


    public Animal(Vector2d position, int simulationId, int energy, Genome genome) {
        this.simulationId = simulationId;
        this.id = UUID.randomUUID();
        this.constants = ConstantsList.getConstants(simulationId);
        this.position = position;
        this.direction = MapDirection.values()[(int) (Math.random() * MapDirection.values().length)];
        this.energy = energy;
        this.genome = genome;
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

    public String toString() {
        return this.direction.toString();
    }

    public boolean isAt (Vector2d position) {
        return this.position.equals(position);
    }

    public void move() {
        direction = direction.rotate(genome.getCurrentGene());
        position.add(direction.toUnitVector());
    }

    public void consume() {
        energy += constants.getENERGY_FROM_PLANT();
    }

    public void removeEnergy(int energyToRemove) {
        energy -= energyToRemove;
    }
}
