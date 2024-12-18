package agh.ics.oop.model.creatures;

import agh.ics.oop.model.info.Constants;
import agh.ics.oop.model.info.ConstantsList;

import java.util.Random;

public class Genome {
    private final int simulationId;
    private final Constants constants;
    private int[] geneList;
    private int startGeneIndex;
    private int moveNumber;

    public Genome (int simulationId) {
        this.simulationId = simulationId;
        this.constants = ConstantsList.getConstants(simulationId);
        this.geneList = new int[constants.getNUMBER_OF_GENES()];

    }

    public int getCurrentGene() {
        int currGeneIndex = (startGeneIndex + moveNumber) % constants.getNUMBER_OF_GENES();
        moveNumber++;
        return geneList[currGeneIndex];
    }

    private void setStartGenomeIndex() {
        Random random = new Random();
        this.startGeneIndex = random.nextInt(8);
    }

    public int[] getGeneList() {
        return geneList;
    }

    //helper functions for tests
    public void setGeneListForTests(int[] geneList) {
        this.geneList = geneList;
    }

    public void setStartGeneIndexForTests(int startGeneIndex) {
        this.startGeneIndex = startGeneIndex;
    }
}
