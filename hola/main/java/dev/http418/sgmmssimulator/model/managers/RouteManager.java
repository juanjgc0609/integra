package dev.http418.sgmmssimulator.model.managers;

import dev.http418.sgmmssimulator.model.*;
import dev.http418.sgmmssimulator.structure.BST;

import java.util.Queue;

public class RouteManager {
    private final BST<Route> routes;
    private final TrafficMap trafficMap;
    public RouteManager(TrafficMap trafficMap) {
        this.routes = new BST<>();
        this.trafficMap = trafficMap;
    }

    public void assignRoute(Vehicle vehicle, Incident incident) {
        if (vehicle.isOnRoute()) {
            return;
        }
        Queue<Tile> pathToIncident = trafficMap.dijkstra(vehicle.getCurrentTile(), incident.getSource());
        if (pathToIncident == null || pathToIncident.isEmpty()) {
            return;
        }
        vehicle.setPath(pathToIncident);
        vehicle.setOnRoute(true);
        incident.switchState();
        Route route = new Route(vehicle, incident, pathToIncident);
        routes.insert(route);
    }

}
