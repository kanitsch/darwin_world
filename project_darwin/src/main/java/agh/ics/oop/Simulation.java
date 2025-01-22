package agh.ics.oop;

import agh.ics.oop.model.ConsoleMapDisplay;
import agh.ics.oop.model.creatures.Animal;
import agh.ics.oop.model.creatures.Genome;
import agh.ics.oop.model.info.Constants;
import agh.ics.oop.model.info.ConstantsList;
import agh.ics.oop.model.maps.WorldMap;
import agh.ics.oop.model.util.Vector2d;

import java.util.Random;

public class Simulation implements Runnable {

    private final WorldMap worldMap;
    private final int sleepTime;
    private boolean stopped = false;
    private boolean ended = false;

    public Simulation(int simulationId, int sleepTime) {
        Constants constants= ConstantsList.getConstants(simulationId);
        this.worldMap = new WorldMap(simulationId);
        this.sleepTime = sleepTime;
        Random rand = new Random();
        for (int i=0; i<constants.getNUMBER_OF_ANIMALS();i++){
            worldMap.place(Animal.startingAnimal(simulationId));
        }

        worldMap.addObserver(new ConsoleMapDisplay());
    }

    private void sleep(int sleepTime){
        try{
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {
//        System.out.println(worldMap);
        sleep(sleepTime);
        while (worldMap.getTotalAnimals()>0){
            if (ended){
                return;
            }
            if (!stopped) {
                worldMap.removeDeadAnimals();
                worldMap.animalsMove();
                worldMap.animalsEat();
                worldMap.animalsBreed();
                worldMap.growGrass();
                worldMap.atMapChanged("Day" + worldMap.getDay()+ "passed");

                sleep(sleepTime);
            }
        }
    }

    public void pause(){
        stopped = true;
    }
    public void resume(){
        stopped = false;
    }
    public void end(){
        ended = true;
    }
    public boolean isStopped(){
        return stopped;
    }
    public int getDay(){
        return this.worldMap.getDay();
    }
    public WorldMap getMap(){
        return worldMap;
    }
}
