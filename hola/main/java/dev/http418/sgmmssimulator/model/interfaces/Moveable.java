package dev.http418.sgmmssimulator.model.interfaces;

import dev.http418.sgmmssimulator.model.Tile;

import java.util.Queue;

public interface Moveable {
    void move();
    boolean hasDestination();
    void setDestination(Tile dest);
    void setPath(Queue<Tile> path);
}
