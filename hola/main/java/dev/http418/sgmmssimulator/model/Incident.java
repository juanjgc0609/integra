package dev.http418.sgmmssimulator.model;

import dev.http418.sgmmssimulator.model.enums.IncidentState;
import dev.http418.sgmmssimulator.model.enums.IncidentType;
import dev.http418.sgmmssimulator.model.interfaces.Drawable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static dev.http418.sgmmssimulator.model.Tile.TILE_SIZE;

public class Incident  implements Comparable<Incident>, Drawable {
    private Tile source;
    private IncidentType type;
    private IncidentState state;
    private long timestamp;
    private boolean blinkOn;
    private long lastBlinkTime;
    private long processStartTime;


    public Incident(Tile source, IncidentType type) {
        this.source = source;
        this.type = type;
        this.state = IncidentState.ACTIVE;
        this.timestamp = System.currentTimeMillis();
    }

    public void switchState() {
        if (state == IncidentState.ACTIVE) {
            state = IncidentState.ASSIGNED;
        }
        else if (state == IncidentState.ASSIGNED) {
            state = IncidentState.PROCESSING;
            processStartTime = System.currentTimeMillis();
        }
        else if (state == IncidentState.PROCESSING) {
            state = IncidentState.RESOLVED;
        }
    }
    public IncidentState getState() {
        return state;
    }

    public Tile getSource() {
        return source;
    }

    public void setSource(Tile source) {
        this.source = source;
    }

    public IncidentType getType() {
        return type;
    }

    public void setType(IncidentType type) {
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int compareTo(Incident other) {
        if (this.type.getPriority() == other.type.getPriority()) {
            return Long.compare(this.timestamp, other.timestamp);
        }
        return Integer.compare(this.type.getPriority(), other.type.getPriority());
    }

    public void draw(GraphicsContext gc, double offsetX, double offsetY) {
        if (state == IncidentState.RESOLVED) {
            return;
        }
        double drawX = source.getX() * TILE_SIZE - offsetX;
        double drawY = source.getY() * TILE_SIZE - offsetY;

        gc.setFill(type.getColor());
        gc.fillOval(
                drawX + TILE_SIZE * 0.25,
                drawY + TILE_SIZE * 0.25,
                TILE_SIZE * 0.5,
                TILE_SIZE * 0.5
        );
        if (state == IncidentState.ACTIVE) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastBlinkTime > 500) {
                blinkOn = !blinkOn;
                lastBlinkTime = currentTime;
            }
            if (blinkOn) {
                gc.setStroke(type.getColor().darker());
                gc.setLineWidth(2);
                gc.strokeOval(
                        drawX + TILE_SIZE * 0.25,
                        drawY + TILE_SIZE * 0.25,
                        TILE_SIZE * 0.5,
                        TILE_SIZE * 0.5
                );
            }
        }
        // if processing, draw a circunference that grows
        else if (state == IncidentState.PROCESSING) {
            long currentTime = System.currentTimeMillis();
            double elapsedTime = (currentTime - processStartTime) / 1000.0; // en segundos

            double maxDiameter = TILE_SIZE * 0.5;
            double maxRadius = maxDiameter / 2;
            double growthRate = maxRadius / 5;
            double radius = Math.min(growthRate * elapsedTime, maxRadius);

            double centerX = drawX + TILE_SIZE / 2.0;
            double centerY = drawY + TILE_SIZE / 2.0;

            gc.setFill(Color.WHITE);
            gc.fillOval(
                    centerX - radius,
                    centerY - radius,
                    radius * 2,
                    radius * 2
            );
        }


    }

    public boolean contains(double mouseX, double mouseY) {
        int drawX = (int) (mouseX / TILE_SIZE);
        int drawY = (int) (mouseY / TILE_SIZE);
        return source.getPoint().equals(new Point2D(drawX, drawY));
    }

}
