package agh.ics.oop.model.util;

import java.util.Iterator;
import java.util.List;

public class PositionIterator implements Iterator<Vector2d> {
    private final List<Vector2d> used;

    public PositionIterator(List<Vector2d> used){
        this.used =used;

    }
    @Override
    public boolean hasNext() {
        return !used.isEmpty();
    }

    @Override
    public Vector2d next() {
        return used.removeFirst();
    }
}
