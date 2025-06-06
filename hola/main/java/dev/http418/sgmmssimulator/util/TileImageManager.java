package dev.http418.sgmmssimulator.util;

import dev.http418.sgmmssimulator.model.enums.TileType;
import javafx.scene.image.Image;

import java.util.EnumMap;
import java.util.Objects;

public class TileImageManager {

    private static final EnumMap<TileType, Image> imageMap = new EnumMap<>(TileType.class);

    static {
        loadImages();
    }

    private static void loadImages() {
        addImage(TileType.ROAD_VERTICAL, "/dev/http418/sgmmssimulator/assets/ROAD_VERTICAL.png");
        addImage(TileType.ROAD_HORIZONTAL, "/dev/http418/sgmmssimulator/assets/ROAD_HORIZONTAL.png");
        addImage(TileType.ROAD_CORNER_NW, "/dev/http418/sgmmssimulator/assets/ROAD_CORNER_NW.png");
        addImage(TileType.ROAD_CORNER_NE, "/dev/http418/sgmmssimulator/assets/ROAD_CORNER_NE.png");
        addImage(TileType.ROAD_CORNER_SW, "/dev/http418/sgmmssimulator/assets/ROAD_CORNER_SW.png");
        addImage(TileType.ROAD_CORNER_SE, "/dev/http418/sgmmssimulator/assets/ROAD_CORNER_SE.png");
        addImage(TileType.ROAD_T_SOUTH, "/dev/http418/sgmmssimulator/assets/ROAD_T_SOUTH.png");
        addImage(TileType.ROAD_T_NORTH, "/dev/http418/sgmmssimulator/assets/ROAD_T_NORTH.png");
        addImage(TileType.ROAD_T_EAST, "/dev/http418/sgmmssimulator/assets/ROAT_T_EAST.png");
        addImage(TileType.ROAD_T_WEST, "/dev/http418/sgmmssimulator/assets/ROAD_T_WEST.png");
        addImage(TileType.STREET_VERTICAL, "/dev/http418/sgmmssimulator/assets/ROAD_VERTICAL.png");
        addImage(TileType.STREET_HORIZONTAL, "/dev/http418/sgmmssimulator/assets/ROAD_HORIZONTAL.png");
        addImage(TileType.STREET_INTERSECTION, "/dev/http418/sgmmssimulator/assets/ROAD_INTERSECTION.png");
        addImage(TileType.HOSPITAL, "/dev/http418/sgmmssimulator/assets/Hospital.jpg");
        addImage(TileType.FIRE_STATION, "/dev/http418/sgmmssimulator/assets/EstaciónDeBomberos.JPG");
        addImage(TileType.POLICE_STATION, "/dev/http418/sgmmssimulator/assets/Policia.JPG");
        addImage(TileType.RESIDENTIAL, "/dev/http418/sgmmssimulator/assets/Building.png");
        addImage(TileType.COMMERCIAL, "/dev/http418/sgmmssimulator/assets/Comercio.JPG");
        addImage(TileType.INDUSTRIAL, "/dev/http418/sgmmssimulator/assets/Factory.JPG");
    }

    private static void addImage(TileType type, String path) {
        imageMap.put(type, new Image(Objects.requireNonNull(TileImageManager.class.getResourceAsStream(path))));
    }

    public static Image getImage(TileType type) {
        return imageMap.get(type);
    }
}