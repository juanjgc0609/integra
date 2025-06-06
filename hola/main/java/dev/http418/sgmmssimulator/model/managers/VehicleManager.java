package dev.http418.sgmmssimulator.model.managers;

import dev.http418.sgmmssimulator.model.Tile;
import dev.http418.sgmmssimulator.model.TrafficMap;
import dev.http418.sgmmssimulator.model.Vehicle;
import dev.http418.sgmmssimulator.model.enums.VehicleType;
import dev.http418.sgmmssimulator.model.interfaces.Drawable;
import javafx.scene.canvas.GraphicsContext;

import java.util.*;

public class VehicleManager implements Drawable {
    private final TrafficMap map;
    private final List<Vehicle> vehicles = new ArrayList<>();
    public static final int MAX_VEHICLES = 10;
    private final Random random;

    public VehicleManager(TrafficMap map) {
        this.map = map;
        random = new Random();
    }

    public void generateVehicle() {
        if (vehicles.size() >= MAX_VEHICLES) {
            return;
        }

        Set<VehicleType> existingTypes = new HashSet<>();
        for (Vehicle v : vehicles) {
            existingTypes.add(v.getType());
        }

        if (existingTypes.size() >= VehicleType.values().length) {
            VehicleType type = VehicleType.values()[random.nextInt(VehicleType.values().length)];
            generateVehicle(type);
        } else {
            for (VehicleType type : VehicleType.values()) {
                if (!existingTypes.contains(type)) {
                    generateVehicle(type);
                    return;
                }
            }
        }
    }

    private void generateVehicle(VehicleType type) {
        ArrayList<Tile> spawnableTiles = map.getSpawnableTiles(type);
        if (spawnableTiles.isEmpty()) {
            return;
        }
        Tile spawnTile = spawnableTiles.get(random.nextInt(spawnableTiles.size()));
        Vehicle newVehicle = new Vehicle(spawnTile, type);
        vehicles.add(newVehicle);
    }

    @Override
    public void draw(GraphicsContext gc, double offsetX, double offsetY) {
        for (Vehicle vehicle : new ArrayList<>(vehicles)) {
            vehicle.draw(gc, offsetX, offsetY);
        }
    }
    public void updateVehicles() {
        for (Vehicle vehicle : new ArrayList<>(vehicles)) {
            vehicle.move();
        }
    }
    public Vehicle getVehicleAt(double mouseX, double mouseY) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.contains(mouseX, mouseY)) {
                return vehicle;
            }
        }
        return null;
    }

    public void removeVehicle(Vehicle vehicle) {
        if (vehicle == null || !vehicles.contains(vehicle)) {
            return;
        }
        vehicles.remove(vehicle);
    }
}
