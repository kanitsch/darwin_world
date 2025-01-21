package agh.ics.oop.model.util;

import agh.ics.oop.model.util.GraphVertex;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

public class DFS {
    private final Stack<GraphVertex> stack = new Stack<>();
    private final HashSet<GraphVertex> visited = new HashSet<>();
    private final List<GraphVertex> descendants = new ArrayList<>();

    public List<GraphVertex> getDescendantsList (GraphVertex startingVertex){
        stack.push(startingVertex);
        visited.add(startingVertex);
        while (!stack.isEmpty()){
            GraphVertex checked = stack.pop();
            List<GraphVertex> children = checked.getChildren();
            for (GraphVertex child : children){
                if (!visited.contains(child)){
                    stack.push(child);
                    visited.add(child);
                    descendants.add(child);
                }
            }
        }
        return descendants;
    }
}