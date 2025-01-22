package agh.ics.oop;

import agh.ics.oop.model.info.Constants;
import agh.ics.oop.model.info.ConstantsList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimulationTest {
    @Test
    public void simulationTest() {
        Constants constants = new Constants(4, 5, 5, 5, 5, false, true, 4, 3, 0, 0, 1, 5, 3, 1);
        ConstantsList.addToConstantsList(0,constants);
        Simulation simulation=new Simulation(0,1);
        simulation.run();

    }

}