package agh.ics.oop.model.creatures;

import agh.ics.oop.model.enums.MapDirection;
import agh.ics.oop.model.info.Constants;
import agh.ics.oop.model.info.ConstantsList;
import agh.ics.oop.model.util.Vector2d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestAnimal {
    @Test
    public void testGetCurrentMove() {
        Constants constants = new Constants(8,10,5,5,5,false,false,0,0,0,0,0,0,0,1);
        ConstantsList.addToConstantsList(0, constants);
        Genome genome = new Genome(0);
        int[] geneList = {1,2,3,4,5,6,7,0};
        genome.setGeneListForTests(geneList);
        genome.setStartGeneIndexForTests(0);
        Animal animal=new Animal(new Vector2d(0,0),0,constants.getSTARTING_ANIMAL_ENERGY(),genome);
        animal.setDirection(MapDirection.W);
        for (int i=0; i<8;i++){
            animal.move();
            System.out.println(animal.getDirection());
            System.out.println(animal.getPosition());
        }

    }
    @Test
    public void descendantsNumber() {
        Constants constants = new Constants(8,10,5,5,5,false,false,0,0,0,0,0,0,0,1);
        ConstantsList.addToConstantsList(0, constants);
        Animal animal1=new Animal(new Vector2d(2,2),0,6,new Genome(0));
        Animal animal2=new Animal(new Vector2d(2,2),0,6,new Genome(0));
        Animal offspring =animal1.breed(animal2);
        offspring.breed(animal1);
        assertEquals(2,animal1.getNumberOfDescendants());
        assertEquals(2,animal2.getNumberOfDescendants());
        assertEquals(1,offspring.getNumberOfDescendants());
    }

}
