package dev.http418.sgmmssimulator.model.managers;

import dev.http418.sgmmssimulator.model.Incident;
import dev.http418.sgmmssimulator.model.Tile;
import dev.http418.sgmmssimulator.model.TrafficMap;
import dev.http418.sgmmssimulator.model.enums.IncidentState;
import dev.http418.sgmmssimulator.model.enums.IncidentType;
import dev.http418.sgmmssimulator.model.interfaces.Drawable;
import dev.http418.sgmmssimulator.structure.BST;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class IncidentManager implements Drawable {
    private final TrafficMap trafficMap;
    private final BST<Incident> incidents;
    public static int MAX_INCIDENTS = 8;

    public IncidentManager(TrafficMap trafficMap) {
        this.trafficMap = trafficMap;
        this.incidents = new BST<>();
    }

    public void generateIncident() {
        if (incidents.size() >= MAX_INCIDENTS) {
            return;
        }
        IncidentType incidentType = Math.random() < 0.5 ? IncidentType.THEFT : IncidentType.FIRE;
        ArrayList<Tile> spawnableTiles = trafficMap.getSpawnableTiles(incidentType);
        spawnableTiles.removeIf(this::tileHasIncident);
        if (spawnableTiles.isEmpty()) {
            return;
        }
        Tile spawnTile = spawnableTiles.get((int) (Math.random() * spawnableTiles.size()));
        Incident incident = new Incident(spawnTile, incidentType);
        incidents.insert(incident);
    }



    public BST<Incident> getIncidents() {
        return incidents;
    }

    @Override
    public void draw(GraphicsContext gc, double offsetX, double offsetY) {
        for (Incident incident : incidents.inOrder()) {
            if (incident.getState() != IncidentState.RESOLVED) {
                incident.draw(gc, offsetX, offsetY);
            }
        }
    }

    public boolean tileHasIncident(Tile tile) {
        for (Incident incident : incidents.inOrder()) {
            if (incident.getSource().equals(tile)) {
                return true;
            }
        }
        return false;
    }

    public Incident getIncidentAt(double mouseX, double mouseY) {
        for (Incident incident : incidents.inOrder()) {
            if (incident.contains(mouseX, mouseY)) {
                return incident;
            }
        }
        return null;
    }
    public void searchAndSwitchIncidents(Tile currentTile) {
        for (Incident incident : incidents.inOrder()) {
            if (incident.getSource().equals(currentTile)) {
                incident.switchState();
                return;
            }
        }
    }
}
