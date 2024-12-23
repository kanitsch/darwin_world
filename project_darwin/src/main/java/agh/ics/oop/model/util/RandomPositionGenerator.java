package agh.ics.oop.model.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class RandomPositionGenerator implements PositionGenerator{
    private final List<Vector2d> used=new ArrayList<Vector2d>();


    public RandomPositionGenerator(int maxWidth, int maxHeight, int numberOfGrass) {
    Random random = new Random();
    List<Vector2d> unused = new ArrayList<Vector2d>();
        for(
    int i = 0;
    i<maxWidth;i++)

    {
        for (int j = 0; j < maxHeight; j++) {
            unused.add(new Vector2d(i, j));
        }
    }

        for(
    int i = 0;
    i<numberOfGrass;i++)

    {
        int size = unused.size();
        int idx = random.nextInt(size);
        Vector2d newPosition = unused.remove(idx);
        used.add(newPosition);
    }}




@Override
public Iterator<Vector2d> iterator() {
    return new PositionIterator(used);
}}

