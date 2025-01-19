package agh.ics.oop.model.maps;

import agh.ics.oop.model.creatures.Animal;
import agh.ics.oop.model.creatures.Genome;
import agh.ics.oop.model.info.Constants;
import agh.ics.oop.model.info.ConstantsList;
import agh.ics.oop.model.util.Vector2d;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class WorldMapTest {

    @Test
    public void placeAnimalOnMap() {
        Constants constants = new Constants(0, 0, 0, 6, 4, false, false, 0, 5, 0, 0, 0, 3, 0, 0);
        ConstantsList.addToConstantsList(0, constants);
        Animal animal = new Animal(new Vector2d(2,2),0,5,new Genome(0));
        WorldMap map = new WorldMap(0);
        map.place(animal);
        assertEquals(new Vector2d(2, 2), animal.getPosition());
    }

    @Test
    public void place2animalsAtTheSamePosition() {
        Constants constants = new Constants(0, 0, 0, 6, 4, true, false, 0, 5, 0, 0, 0, 3, 0, 0);
        ConstantsList.addToConstantsList(0, constants);
        Animal aniimal1 = new Animal(new Vector2d(2,2),0,5,new Genome(0));
        Animal animal2 = new Animal(new Vector2d(2,2),0,5,new Genome(0));
        WorldMap map = new WorldMap(0);
        map.place(aniimal1);
        map.place(animal2);
        assertEquals(new Vector2d(2,2), animal2.getPosition());
        assertEquals(new Vector2d(2,2), aniimal1.getPosition());
    }

    @Test
    public void moveAnimal() {
        Constants constants = new Constants(1, 1, 0, 6, 4, true, false, 0, 5, 1, 0, 0, 3, 0, 0);
        ConstantsList.addToConstantsList(0, constants);
        int[] genesList = {1};
        Genome genome = new Genome(0);
        genome.setStartGeneIndexForTests(0);
        genome.setGeneListForTests(genesList);
        Animal animal = new Animal(new Vector2d(2,2),0,5,genome);
        WorldMap map = new WorldMap(0);
        map.place(animal);
        map.animalsMove();
        System.out.println(animal.getPosition());

    }

    @Test
    public void removeDeadAnimal() {
        Constants constants = new Constants(1, 1, 0, 6, 4, true, false, 0, 0, 1, 0, 0, 0, 0, 0);
        ConstantsList.addToConstantsList(0, constants);
        Animal animal = new Animal(new Vector2d(2,2),0,0,new Genome(0));
        Animal livingAnimal = new Animal(new Vector2d(3,2),0,5,new Genome(0));
        WorldMap map = new WorldMap(0);
        map.place(animal);
        map.place(livingAnimal);
        map.removeDeadAnimals();
        assertNull(map.objectAt(new Vector2d(2, 2)));
        assertEquals(livingAnimal, map.objectAt(new Vector2d(3, 2)));
        assertEquals(0,map.getAverageLifeSpan());

    }

    @Test
    public void breedingOnMap(){
        Constants constants = new Constants(1, 5, 0, 6, 4, true, false, 0, 5, 1, 0, 0, 3, 3, 2);
        ConstantsList.addToConstantsList(0, constants);
        Animal animal1 = new Animal(new Vector2d(2,2),0,5,new Genome(0));
        Animal animal2 = new Animal(new Vector2d(2,2),0,5,new Genome(0));
        WorldMap map = new WorldMap(0);
        map.place(animal1);
        map.place(animal2);
//        map.animalsBreed();
        System.out.println(map.getTotalAnimals());
        System.out.println(map.getAverageEnergy());
        System.out.println(map.getAverageNumberOfChildren());

    }

    @Test
    public void growGrassTest(){
        Constants constants = new Constants(0, 0, 0, 10, 10, true, false, 0, 5, 0, 0, 0, 20, 0, 0);
        ConstantsList.addToConstantsList(0, constants);
        WorldMap map = new WorldMap(0);
        assertEquals(5,map.getTotalPlants());
        map.growGrass();
        System.out.println(map);
        assertEquals(25,map.getTotalPlants());
    }

    @Test
    public void animalsEatTest(){
        Constants constants = new Constants(0, 0, 2, 3, 3, true, false, 0, 15, 1, 0, 0, 3, 0, 0);
        ConstantsList.addToConstantsList(0, constants);
        WorldMap map = new WorldMap(0);
        Animal animal1 = new Animal(new Vector2d(2,2),0,5,new Genome(0));
        map.place(animal1);
        map.animalsEat();
        assertEquals(14, map.getTotalPlants());
        assertEquals(7,animal1.getEnergy());
    }

  
}
