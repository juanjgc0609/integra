package dev.http418.sgmmssimulator.model;

import dev.http418.sgmmssimulator.model.enums.VehicleType;
import dev.http418.sgmmssimulator.model.interfaces.Drawable;
import dev.http418.sgmmssimulator.model.interfaces.Moveable;
import dev.http418.sgmmssimulator.model.managers.GameState;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import static dev.http418.sgmmssimulator.model.Tile.TILE_SIZE;


public class Vehicle implements Drawable, Moveable {
    protected Point2D position;
    private final VehicleType type;
    private Queue<Tile> path;
    private Tile currentTile;
    private Tile nextTile;
    private final double speed;
    private static final double MIN_DISTANCE = 2.0;
    private long lastBlinkTime;
    private boolean blinkOn;
    private boolean onRoute;
    private double rotationAngle;
    private boolean selected;
    private long incidentStartTime;

    public Vehicle(Tile tile, VehicleType type) {
        this.currentTile = tile;
        this.position = tile.getMapPosition();
        this.type = type;
        onRoute = false;
        path = new LinkedList<>();
        this.speed = type.getSpeed();
        this.rotationAngle = 0;
        this.incidentStartTime = 0;
        this.selected = false;
    }
    public Tile getCurrentTile() {
        return currentTile;
    }
    public void setOnRoute(boolean onRoute) {
        this.onRoute = onRoute;
    }

    @Override
    public void draw(GraphicsContext gc, double offsetX, double offsetY) {
        double drawX = position.getX() - offsetX;
        double drawY = position.getY() - offsetY;

        if (drawX >= -TILE_SIZE && drawY >= -TILE_SIZE &&
                drawX < gc.getCanvas().getWidth() && drawY < gc.getCanvas().getHeight()) {
            gc.save();
            gc.translate(drawX + (double) TILE_SIZE / 2, drawY + (double) TILE_SIZE / 2);
            gc.rotate(rotationAngle);
            if (onRoute) {
                gc.setGlobalAlpha(0.5);
            }
            gc.drawImage(type.getImage(), (double) -TILE_SIZE / 2, (double) -TILE_SIZE / 2, TILE_SIZE, TILE_SIZE);
            gc.restore();
        }

        if (!onRoute) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastBlinkTime > 500) {
                blinkOn = !blinkOn;
                lastBlinkTime = currentTime;
            }

            if (blinkOn || selected) {
                gc.setStroke(selected ? Color.GREEN : Color.LIGHTBLUE);
                gc.setLineWidth(2);
                gc.strokeOval(
                        drawX + (double) TILE_SIZE / 8,
                        drawY + (double) TILE_SIZE / 8,
                        (double) TILE_SIZE * 3 / 4,
                        (double) TILE_SIZE * 3 / 4
                );
            }
        }
    }

    @Override
    public void move() {
        if (onRoute && nextTile == null && path.isEmpty()) {
            // Wait 5 seconds to resolve incident, then remove vehicle from route
            if (incidentStartTime == 0) {
                incidentStartTime = System.currentTimeMillis();
                GameState.getInstance().getIncidentManager().searchAndSwitchIncidents(currentTile);
            } else if (System.currentTimeMillis() - incidentStartTime >= 5000) {
                onRoute = false;
                incidentStartTime = 0;
                GameState.getInstance().getVehicleManager().removeVehicle(this);
                GameState.getInstance().getIncidentManager().searchAndSwitchIncidents(currentTile);
            }
            return;
        }
        if (nextTile == null && !path.isEmpty()) {
            nextTile = path.poll();
        }
        if (nextTile != null) {
            Point2D targetPos = nextTile.getMapPosition();
            Point2D direction = targetPos.subtract(position);
            double distance = direction.magnitude();
            rotationAngle = Math.toDegrees(Math.atan2(direction.getY(), direction.getX())) + 90;
            if (distance < MIN_DISTANCE) {
                position = targetPos;
                currentTile = nextTile;
                nextTile = path.isEmpty() ? null : path.poll();
            } else {
                direction = direction.normalize().multiply(speed* currentTile.getSpeedModifier());
                position = position.add(direction);
            }
        } else {
            wander();
        }
    }

    public VehicleType getType() {
        return type;
    }
    @Override
    public void setDestination(Tile dest) {

    }

    @Override
    public boolean hasDestination() {
        return nextTile != null || !path.isEmpty();
    }

    @Override
    public void setPath(Queue<Tile> newPath) {
        this.path.clear();
        this.path.addAll(newPath);
        if (nextTile == null && !this.path.isEmpty()) {
            nextTile = this.path.poll();
        }
    }

    private void wander() {
        // Implement wandering behavior using neighbors of current tile
        TrafficMap map = GameState.getInstance().getTrafficMap();
        List<Tile> neighbors = map.getNeighbors(currentTile);
        List<Tile> traversableNeighbors = neighbors.stream()
                .filter(Tile::isTraversable)
                .toList();

        if (!traversableNeighbors.isEmpty()) {
            Random random = new Random();
            Tile randomTile = traversableNeighbors.get(random.nextInt(traversableNeighbors.size()));
            Queue<Tile> newPath = new LinkedList<>();
            newPath.add(randomTile);
            setPath(newPath);
        }
    }

    public boolean canHandleIncident(Incident incident) {
        // Check if the vehicle can handle the incident based on its type
        return type.getIncidentTypes().equals(incident.getType());
    }

    public boolean contains(double mouseX, double mouseY) {
        double centerX = position.getX() + TILE_SIZE / 2.0;
        double centerY = position.getY() + TILE_SIZE / 2.0;
        double radius = TILE_SIZE * 0.375;

        double dx = mouseX - centerX;
        double dy = mouseY - centerY;
        return dx * dx + dy * dy <= radius * radius;
    }



    public boolean isOnRoute() {
        return onRoute;
    }

    public void setSelected(boolean s) {
        this.selected = s;
    }
}
