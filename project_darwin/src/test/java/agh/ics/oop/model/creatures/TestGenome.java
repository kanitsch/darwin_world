package agh.ics.oop.model.creatures;

import agh.ics.oop.model.info.Constants;
import agh.ics.oop.model.info.ConstantsList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Console;

public class TestGenome {

    @Test
    public void testgetCurrentGene() {
        int[] geneList = {1,2,3};
        Constants constants = new Constants(3,12,12);
        ConstantsList.addToConstantsList(1, constants);
        Genome genome = new Genome(1);
        genome.setStartGeneIndexForTests(0);
        genome.setGeneListForTests(geneList);

        Assertions.assertEquals(1, genome.getCurrentGene());
        Assertions.assertEquals(2,genome.getCurrentGene());
        Assertions.assertEquals(3,genome.getCurrentGene());

    }
}
