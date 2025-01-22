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
    private int day=0;

    public Simulation(int simulationId, int sleepTime) {
        Constants constants= ConstantsList.getConstants(simulationId);
        this.worldMap = new WorldMap(simulationId);
        this.sleepTime = sleepTime;
        Random rand = new Random();
        for (int i=0; i<constants.getNUMBER_OF_ANIMALS();i++){
            int x=rand.nextInt(constants.getMAP_WIDTH()+1);
            int y=rand.nextInt(constants.getMAP_HEIGHT()+1);
            worldMap.place(new Animal(new Vector2d(x,y),simulationId,constants.getSTARTING_ANIMAL_ENERGY(),new Genome(simulationId)));
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
        System.out.println(worldMap);
        sleep(sleepTime);
        day++;
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
                this.day++;
                worldMap.atMapChanged("Day" + (day - 1) + "passed");

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
        return day;
    }
    public WorldMap getMap(){
        return worldMap;
    }
}
