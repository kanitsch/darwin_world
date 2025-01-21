package agh.ics.oop.model.creatures;

import agh.ics.oop.model.info.Constants;
import agh.ics.oop.model.info.ConstantsList;

import java.util.*;

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

    public int getActivatedGene(){
        int currentGene = getCurrentGene();
        moveNumber--;
        return currentGene;
    }

    private void setStartGenomeIndex() {
        Random random = new Random();
        this.startGeneIndex = random.nextInt(8);
    }

    public int[] getGeneList() {
        return geneList;
    }

    //mutation of only one gene
    public void mutateGene(int geneIndex) {
        Random random = new Random();
        if (!constants.isCOMPLETE_RANDOM()) {
            int[] multiplier = {-1, 1};
            geneList[geneIndex] = (geneList[geneIndex] + multiplier[random.nextInt(2)] + 8) % 8;
        }
        else {
            geneList[geneIndex] = random.nextInt(8);
        }
    }

    //mutation of random amount of genes from a range MIN - MAX
    public void mutate() {
        Random random = new Random();
        int range = constants.getMAX_MUTATIONS() - constants.getMIN_MUTATIONS();
        int numberOfMutations = range > 0 ? random.nextInt(range) + constants.getMIN_MUTATIONS() : constants.getMIN_MUTATIONS();
        List<Integer> indexes = new ArrayList<Integer>();
        for (int i = 0; i < constants.getNUMBER_OF_GENES(); i++) {
            indexes.add(i);
        }

        Collections.shuffle(indexes);
        List<Integer> indexesToMutate = indexes.subList(0, numberOfMutations);

        for (int index : indexesToMutate) {
            mutateGene(index);
        }
    }

    public  static Genome startingAnimalGenome (int simulationId) {
        Random random = new Random();
        Genome genome = new Genome(simulationId);
        for (int i = 0; i < genome.constants.getNUMBER_OF_GENES(); i++) {
            genome.geneList[i] = random.nextInt(8);
        }

        genome.setStartGenomeIndex();
        return genome;
    }

    public static Genome offspringAnimalGenome (Animal mother, Animal father) {
        Random random = new Random();
        Genome offspringGenome = new Genome(father.getSimulationId());
        Animal dominant;
        Animal minor;
        if (mother.getEnergy() > father.getEnergy()) {
            dominant = mother;
            minor = father;
        }
        else {
            dominant = father;
            minor = mother;
        }

        double splitProportion = (double) offspringGenome.constants.getNUMBER_OF_GENES() / (father.getEnergy() + mother.getEnergy());
        int dominantGeneCount = (int) (splitProportion * dominant.getEnergy());
        int minorGeneCount = (int) (splitProportion * minor.getEnergy());
        System.out.println(dominantGeneCount + " | " + minorGeneCount);
        System.out.println(splitProportion);
        System.out.println(dominant.getEnergy() + " | " + minor.getEnergy());


        boolean dominatFromLeft = random.nextBoolean();
        if (dominatFromLeft) {
            System.arraycopy(dominant.getGenome().geneList, 0, offspringGenome.geneList, 0, dominantGeneCount);
            System.arraycopy(minor.getGenome().geneList, dominantGeneCount, offspringGenome.geneList, dominantGeneCount, minorGeneCount);
        }
        else {
            System.arraycopy(minor.getGenome().geneList, 0, offspringGenome.geneList, 0, minorGeneCount);
            System.arraycopy(dominant.getGenome().geneList, minorGeneCount, offspringGenome.geneList, minorGeneCount, dominantGeneCount);
        }

        offspringGenome.mutate();
        offspringGenome.setStartGenomeIndex();

        return offspringGenome;
    }


    //helper functions for tests
    public void setGeneListForTests(int[] geneList) {
        this.geneList = geneList;
    }

    public void setStartGeneIndexForTests(int startGeneIndex) {
        this.startGeneIndex = startGeneIndex;
    }


    public void mutateGeneForTests(int geneIndex) {
        Random random = new Random();
        if (!constants.isCOMPLETE_RANDOM()) {
            geneList[geneIndex]++;
        }
        else {
            geneList[geneIndex] = 4;
        }
    }

    public void mutateForTests() {
        Random random = new Random();
        int numberOfMutations = random.nextInt(constants.getMAX_MUTATIONS() - constants.getMIN_MUTATIONS()) + constants.getMIN_MUTATIONS();
        List<Integer> indexes = new ArrayList<Integer>();
        for (int i = 0; i < constants.getNUMBER_OF_GENES(); i++) {
            indexes.add(i);
        }

        System.out.println(numberOfMutations);
        Collections.shuffle(indexes);
        List<Integer> indexesToMutate = indexes.subList(0, numberOfMutations);
        for (int index : indexesToMutate) {
            System.out.print(index + "|");
            mutateGeneForTests(index);
        }
        System.out.println("\n");
        for (int gene : geneList) {
            System.out.print(gene + ":");

        }

    }


}
