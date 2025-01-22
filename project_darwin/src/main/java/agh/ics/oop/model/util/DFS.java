package agh.ics.oop.model.util;

import agh.ics.oop.model.creatures.Animal;
import agh.ics.oop.model.util.GraphVertex;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

public class DFS {
    private final Stack<Animal> stack = new Stack<>();
    private final HashSet<Animal> visited = new HashSet<>();
    private final List<Animal> descendants = new ArrayList<>();

    public List<Animal> getDescendantsList(Animal startingVertex){
        stack.push(startingVertex);
        visited.add(startingVertex);
        while (!stack.isEmpty()){
            Animal checked = stack.pop();
            List<Animal> children = checked.getDescendants();
            for (Animal child : children){
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