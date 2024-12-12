package agh.ics.oop.model;

import agh.ics.oop.model.util.MapVisualizer;

import java.util.Map;

public class Animal implements WorldElement{
    private Vector2d position;
    private MapDirection direction;

    public Animal() {
        this.position = new Vector2d(2,2);
        this.direction = MapDirection.NORTH;
    }
    public Animal(Vector2d position, MapDirection direction) {
        this.position = position;
        this.direction = direction;
    }

    public MapDirection getDirection() {
        return this.direction;
    }

    public Vector2d getPosition() {
        return this.position;
    }

    public String toString() {
        return this.direction.toString();
    }

    public boolean isAt (Vector2d position) {
        return this.position.equals(position);
    }

    public void move(MoveDirection direction, WorldMap map) {
        Vector2d potentialPosition;
        switch (direction) {
            case RIGHT:
                this.direction = this.direction.next();
                break;
            case LEFT:
                this.direction = this.direction.previous();
                break;
            case FORWARD:
                potentialPosition = this.position.add(this.direction.toUnitVector());
                if (map.canMoveTo(potentialPosition)) {
                    this.position = potentialPosition;
                }
                break;
            case BACKWARD:
                potentialPosition = this.position.subtract(this.direction.toUnitVector());
                if (map.canMoveTo(potentialPosition)) {
                    this.position = potentialPosition;
                }
                break;
            default:
                break;
        }
    }
}
