package dev.http418.sgmmssimulator.model;

import java.util.Queue;

public class Route implements Comparable<Route> {
    private Vehicle vehicle;
    private Incident incident;
    private final Queue<Tile> path;
    private final long duration;
    private long remaningTime;

    public Route(Vehicle vehicle, Incident incident, Queue<Tile> path) {
        this.vehicle = vehicle;
        this.incident = incident;
        this.path = path;
        this.duration = calculateDuration();
        this.remaningTime = duration;
    }


    private long calculateDuration() {

        long pathSize = path.size();
        double speed = vehicle.getType().getSpeed();
        if (speed <= 0) {
            return Long.MAX_VALUE;
        }
        return (long) (pathSize / speed * 1000);
    }

    public void updateRemainingTime(long elapsedTime) {
        this.remaningTime -= elapsedTime;
        if (this.remaningTime < 0) {
            this.remaningTime = 0;
        }
    }


    @Override
    public int compareTo(Route other) {
        return Long.compare(this.duration, other.duration);
    }
}
