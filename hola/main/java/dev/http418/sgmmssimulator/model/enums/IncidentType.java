package dev.http418.sgmmssimulator.model.enums;

import javafx.scene.paint.Color;

import java.util.Set;

public enum IncidentType {
    THEFT(1, Color.DARKSLATEBLUE),
    FIRE(2, Color.ORANGERED),
    ACCIDENT(3, Color.GOLD);

    private final int priority;
    private final Color color;

    IncidentType(int priority, Color color) {
        this.priority = priority;
        this.color = color;
    }

    public int getPriority() {
        return priority;
    }

    public Color getColor() {
        return color;
    }

    public Set<TileType> getValidSpawnTileTypes() {
        return switch (this) {
            case THEFT -> Set.of(TileType.RESIDENTIAL, TileType.COMMERCIAL);
            case FIRE -> Set.of(TileType.RESIDENTIAL, TileType.INDUSTRIAL);
            case ACCIDENT -> Set.of(TileType.ROAD_VERTICAL, TileType.ROAD_HORIZONTAL,
                    TileType.ROAD_CORNER_NW, TileType.ROAD_CORNER_NE,
                    TileType.ROAD_CORNER_SW, TileType.ROAD_CORNER_SE,
                    TileType.ROAD_T_SOUTH, TileType.ROAD_T_NORTH,
                    TileType.ROAD_T_EAST, TileType.ROAD_T_WEST,
                    TileType.STREET_VERTICAL, TileType.STREET_HORIZONTAL,
                    TileType.STREET_INTERSECTION);
        };
    }
    public String getDisplayName() {
        return switch (this) {
            case THEFT -> "Theft";
            case FIRE -> "Fire";
            case ACCIDENT -> "Accident";
        };
    }
}
