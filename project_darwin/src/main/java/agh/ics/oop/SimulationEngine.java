package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;

public class SimulationEngine {
    private List<Simulation> simulations;
    private List<Thread> threads;
    //czy mozna w trakcie dodawac nowe symulacje?

    public SimulationEngine(List<Simulation> simulations) {
        this.simulations = simulations;
        this.threads = new ArrayList<>();
    }
    public void runAsync(){
        for (Simulation simulation : simulations) {
            Thread thread = new Thread(simulation);
            thread.start();
            threads.add(thread);
        }
    }

    public void waitForCompletion() {
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted: " + thread.getName());
                Thread.currentThread().interrupt();
            }
        }
    }

    public void endSimulations() {
        for (Simulation simulation : simulations) {
            simulation.end();
        }
    }
}
