package dev.http418.sgmmssimulator.model.enums;

import javafx.scene.image.Image;

import java.util.Objects;

public enum VehicleType {
    FIRE_TRUCK(1.7, "/dev/http418/sgmmssimulator/assets/fire_truck.png"),
    POLICE_CAR(1.6, "/dev/http418/sgmmssimulator/assets/police_car.png"),
    AMBULANCE(1.9, "/dev/http418/sgmmssimulator/assets/ambulance.png");

    private final double speed;
    private final Image image;

    VehicleType(double speed, String imagePath) {
        this.speed = speed;
        this.image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
    }

    public double getSpeed() {
        return speed;
    }

    public Image getImage() {
        return image;
    }
    public TileType getSpawnTileType() {
        return switch (this) {
            case FIRE_TRUCK -> TileType.FIRE_STATION;
            case POLICE_CAR -> TileType.POLICE_STATION;
            case AMBULANCE -> TileType.HOSPITAL;
        };
    }

    public IncidentType getIncidentTypes() {
        return switch (this) {
            case FIRE_TRUCK -> IncidentType.FIRE;
            case POLICE_CAR -> IncidentType.THEFT;
            case AMBULANCE -> IncidentType.ACCIDENT;
        };
    }
    public String getDisplayName() {
        return switch (this) {
            case FIRE_TRUCK -> "Fire Truck";
            case POLICE_CAR -> "Police Car";
            case AMBULANCE -> "Ambulance";
        };
    }
}
