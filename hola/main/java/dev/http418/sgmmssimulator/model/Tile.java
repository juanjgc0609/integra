package dev.http418.sgmmssimulator.model;

import dev.http418.sgmmssimulator.model.enums.TileType;
import dev.http418.sgmmssimulator.util.TileImageManager;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Tile {
    private Point2D point;
    private TileType type;
    public static final int TILE_SIZE = 48;

    public Tile(double x, double y, TileType type) {
        this.point = new Point2D(x, y);
        this.type = type;
    }

    public int getX() {
        return (int) point.getX();
    }
    public int getY() {
        return (int) point.getY();
    }


    public double getSpeedModifier() {
        return switch (type) {
            case ROAD_VERTICAL, ROAD_HORIZONTAL, ROAD_CORNER_NE, ROAD_CORNER_NW, ROAD_CORNER_SE, ROAD_CORNER_SW,
                 ROAD_T_EAST, ROAD_T_NORTH, ROAD_T_SOUTH, ROAD_T_WEST  -> 1.0;
            case STREET_HORIZONTAL, STREET_INTERSECTION, STREET_VERTICAL -> 0.75;
            default -> 0.0;
        };
    }

    public Point2D getMapPosition() {
        return new Point2D(point.getX() * TILE_SIZE, point.getY() * TILE_SIZE);
    }

    public Point2D getPoint() {
        return point;
    }

    public void setPoint(Point2D point) {
        this.point = point;
    }

    public TileType getType() {
        return type;
    }


    public void draw(GraphicsContext gc, double drawX, double drawY) {
        Image image = TileImageManager.getImage(type);
        if (image != null) {
            gc.drawImage(image, drawX, drawY, TILE_SIZE, TILE_SIZE);
        } else {
            gc.setFill(Color.GRAY);
            gc.fillRect(drawX, drawY, TILE_SIZE, TILE_SIZE);
        }
    }

    public boolean isTraversable() {
        return switch (type) {
            case ROAD_VERTICAL, ROAD_HORIZONTAL, ROAD_CORNER_NE, ROAD_CORNER_NW, ROAD_CORNER_SE, ROAD_CORNER_SW,
                 ROAD_T_EAST, ROAD_T_NORTH, ROAD_T_SOUTH, ROAD_T_WEST , STREET_HORIZONTAL, STREET_INTERSECTION,
                 STREET_VERTICAL -> true;
            default -> false;
        };
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Tile other)) return false;
        return this.getX() == other.getX() && this.getY() == other.getY();
    }

    @Override
    public String toString() {
        return "Tile{" +
                "point=" + point +
                ", type=" + type +
                '}';
    }
}
