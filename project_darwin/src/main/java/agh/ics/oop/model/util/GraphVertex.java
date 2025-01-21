package agh.ics.oop.model.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GraphVertex {
    private final UUID id;
    private final List<GraphVertex> children = new ArrayList<>();

    public GraphVertex(UUID id){
        this.id = id;
    }

    public void addChild (GraphVertex child){
        children.add(child);
    }

    public UUID getId() {
        return id;
    }

    public List<GraphVertex> getChildren() {
        return children;
    }
}