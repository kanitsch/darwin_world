package agh.ics.oop;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SimulationEngine {
    private final List<Simulation> simulations;
    private final List<Thread> threads;

    public SimulationEngine(List<Simulation> simulations) {
        this.simulations = new CopyOnWriteArrayList<>(simulations);
        this.threads = new CopyOnWriteArrayList<>();
    }

    public void runAsync() {
        synchronized (simulations) {
            for (Simulation simulation : simulations) {
                Thread thread = new Thread(simulation);
                thread.start();
                threads.add(thread);
            }
        }
    }

    public void addSimulation(Simulation simulation) {
        synchronized (simulations) {
            simulations.add(simulation);
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

