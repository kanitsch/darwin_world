
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
    private int dateOfDeath=-1;
    private List<Animal> children =new ArrayList<Animal>();


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

    public int getDateOfDeath() {return this.dateOfDeath;}

    public void setDateOfDeath(int date) {this.dateOfDeath = date;}

    public List<Animal> getChildren() {
        return children;
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
        Random rand = new Random();
        int x=rand.nextInt(constants.getMAP_WIDTH()+1);
        int y=rand.nextInt(constants.getMAP_HEIGHT()+1);
        Vector2d position = new Vector2d(x,y);
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
        this.children.add(offspring);
        partner.childrenNumber++;
        partner.children.add(offspring);

        return offspring;
    }

    public List<Animal> getDescendantsList(Animal startingVertex){
        Stack<Animal> stack = new Stack<>();
        HashSet<Animal> visited = new HashSet<>();
        List<Animal> descendants = new ArrayList<>();
        stack.push(startingVertex);
        visited.add(startingVertex);
        while (!stack.isEmpty()){
            Animal checked = stack.pop();
            List<Animal> children = checked.getChildren();
            for (Animal child : children){
                if (!visited.contains(child)){
                    stack.push(child);
                    visited.add(child);
                    descendants.add(child);
                }
            }
        }
        return descendants;
    }

    public int getNumberOfDescendants() {
        List<Animal> descendants =getDescendantsList(this);
        return descendants.size();
    }






}
