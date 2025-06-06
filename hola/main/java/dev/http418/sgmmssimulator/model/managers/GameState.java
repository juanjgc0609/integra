package dev.http418.sgmmssimulator.model.managers;

import dev.http418.sgmmssimulator.model.Incident;
import dev.http418.sgmmssimulator.model.TrafficMap;
import dev.http418.sgmmssimulator.model.Vehicle;
import dev.http418.sgmmssimulator.model.interfaces.Drawable;
import dev.http418.sgmmssimulator.util.FileManager;
import javafx.scene.canvas.GraphicsContext;

public class GameState implements Drawable {
    private static GameState instance;
    private final TrafficMap trafficMap;
    private final VehicleManager vehicleManager;
    private final IncidentManager incidentManager;
    private final RouteManager routeManager;
    private Vehicle selectedVehicle;

    private double offsetX = 0;
    private double offsetY = 0;
    private long lastVehicleGenerationTime = 0;
    private long lastIncidentGenerationTime = 0;

    private GameState() {
        this.trafficMap = new TrafficMap(FileManager.loadMapData());
        this.vehicleManager = new VehicleManager(trafficMap);
        this.incidentManager = new IncidentManager(trafficMap);
        this.routeManager = new RouteManager(trafficMap);
    }

    public static synchronized GameState getInstance() {
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }
    public TrafficMap getTrafficMap() { return trafficMap; }
    public VehicleManager getVehicleManager() { return vehicleManager; }
    public IncidentManager getIncidentManager() { return incidentManager; }

    @Override
    public void draw(GraphicsContext gc, double offsetX, double offsetY) {
        trafficMap.draw(gc, offsetX, offsetY);
        vehicleManager.draw(gc, offsetX, offsetY);
        incidentManager.draw(gc, offsetX, offsetY);
    }
   public String handleMouseClick(double mouseX, double mouseY) {
        if (selectedVehicle != null) {
            Incident incident = incidentManager.getIncidentAt(mouseX, mouseY);
            if (incident != null && selectedVehicle.canHandleIncident(incident)) {
                // Assign the route to the selected vehicle
                routeManager.assignRoute(selectedVehicle, incident);
                selectedVehicle.setSelected(false);
                selectedVehicle = null;
                return "Greatly! now you can see the vehicle moving towards the incident. "
                        + "Click on another vehicle to select";
            }
            selectedVehicle.setSelected(false);
            selectedVehicle = null;
            if (incident != null) {
                return "The vehicle is not suitable for this incident. "
                        + "Please select a different vehicle.";
            }
            return "You deselected the vehicle. Click on a vehicle to select it again.";
        } else {
            Vehicle vehicle = vehicleManager.getVehicleAt(mouseX, mouseY);
            if (vehicle != null) {
                selectedVehicle = vehicle;
                vehicle.setSelected(true);
                return "You selected a " + vehicle.getType().getDisplayName()
                        + ". Click on an "+vehicle.getType().getIncidentTypes().getDisplayName()+" to assign it to this vehicle.";
            }
            return "No vehicle found at the clicked position. "
                    + "Click on a vehicle to select it or click on an incident to assign it to a vehicle.";
        }
   }

    public double getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(double offsetX) {
        this.offsetX = offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(double offsetY) {
        this.offsetY = offsetY;
    }

    public long getLastVehicleGenerationTime() {
        return lastVehicleGenerationTime;
    }

    public void setLastVehicleGenerationTime(long lastVehicleGenerationTime) {
        this.lastVehicleGenerationTime = lastVehicleGenerationTime;
    }

    public long getLastIncidentGenerationTime() {
        return lastIncidentGenerationTime;
    }

    public void setLastIncidentGenerationTime(long lastIncidentGenerationTime) {
        this.lastIncidentGenerationTime = lastIncidentGenerationTime;
    }
}