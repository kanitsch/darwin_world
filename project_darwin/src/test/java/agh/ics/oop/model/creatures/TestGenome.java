package agh.ics.oop.model.creatures;

import agh.ics.oop.model.info.Constants;
import agh.ics.oop.model.info.ConstantsList;
import agh.ics.oop.model.util.Vector2d;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Console;

public class TestGenome {

    @Test
    public void testgetCurrentGene() {
        int[] geneList = {1,2,3};
        Constants constants = new Constants(
                3,
                12,
                12,
                0,
                0,
                false,
                false,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0
        );
        ConstantsList.addToConstantsList(1, constants);
        Genome genome = new Genome(1);
        genome.setStartGeneIndexForTests(0);
        genome.setGeneListForTests(geneList);

        Assertions.assertEquals(1, genome.getCurrentGene());
        Assertions.assertEquals(2,genome.getCurrentGene());
        Assertions.assertEquals(3,genome.getCurrentGene());

    }
    @Test
    public void testMutateGene() {
        int[] geneList = {1,2,4};
        Constants constants = new Constants(
                3,
                12,
                12,
                0,
                0,
                false,
                false,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0
        );        ConstantsList.addToConstantsList(1, constants);
        Genome genome = new Genome(1);
        genome.setStartGeneIndexForTests(0);
        genome.setGeneListForTests(geneList);

        genome.mutateGeneForTests(0);
        Assertions.assertEquals(2, genome.getCurrentGene());
        genome.mutateGeneForTests(1);
        Assertions.assertEquals(3,genome.getCurrentGene());
        genome.mutateGeneForTests(2);
        Assertions.assertEquals(5,genome.getCurrentGene());
    }

    @Test
    public void testMutate() {
        int[] geneList = {1,2,4,2,4,5,6,7,8,2};
        Constants constants = new Constants(
                3,
                12,
                12,
                0,
                0,
                false,
                false,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0
        );        ConstantsList.addToConstantsList(1, constants);
        Genome genome = new Genome(1);
        genome.setGeneListForTests(geneList);
        genome.mutateForTests();
    }

    @Test
    public void testStartingAnimalGenome () {
        Constants constants = new Constants(
                3,
                12,
                12,
                0,
                0,
                false,
                false,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0
        );

        for (int i = 0; i < 10; i++) {
            ConstantsList.addToConstantsList(i, constants);
            Genome genome = Genome.startingAnimalGenome(i);
            int[] genes =  genome.getGeneList();
            Genome.startingAnimalGenome(i);
            for (int gene: genes) System.out.print(gene + "|");
            System.out.println();
        }

    }

    @Test
    public void testOffspirngAnimalGenome () {
        Constants constants = new Constants(
                3,
                12,
                12,
                0,
                0,
                false,
                false,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                0
        );
        ConstantsList.addToConstantsList(1, constants);

        Genome fatherGenome = Genome.startingAnimalGenome(1);
        Animal father = new Animal(new Vector2d(1,1), 1,150,fatherGenome);

        Genome motherGenome = Genome.startingAnimalGenome(1);
        Animal mother = new Animal(new Vector2d(1,1), 1, 50,motherGenome);

        Genome offspringGenom = Genome.offspringAnimalGenome(mother, father);

        for (int gene: fatherGenome.getGeneList()) System.out.print(gene + "|");
        System.out.println();

        for (int gene: motherGenome.getGeneList()) System.out.print(gene + "|");
        System.out.println();


        for (int gene: offspringGenom.getGeneList()) System.out.print(gene + "|");
        System.out.println();
    }
}
