package dev.http418.sgmmssimulator.controller;

import dev.http418.sgmmssimulator.model.Tile;
import dev.http418.sgmmssimulator.model.managers.GameState;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class TrafficMapController implements Initializable {

    @FXML private Canvas canvasElement;
    @FXML private Label consoleLabel;

    private final double SCROLL_SPEED = 5.0;
    private final long VEHICLE_GENERATION_INTERVAL = 10_000;
    private final long INCIDENT_GENERATION_INTERVAL = 15_000;

    private GameState gameState;
    private Set<KeyCode> keysPressed;
    private int mapPixelWidth;
    private int mapPixelHeight;
    private AnimationTimer animationTimer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameState = GameState.getInstance();
        keysPressed = new HashSet<>();

        int mapWidthTiles = gameState.getTrafficMap().getWidth();
        int mapHeightTiles = gameState.getTrafficMap().getHeight();
        mapPixelWidth = mapWidthTiles * Tile.TILE_SIZE;
        mapPixelHeight = mapHeightTiles * Tile.TILE_SIZE;

        canvasElement.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(e -> keysPressed.add(e.getCode()));
                newScene.setOnKeyReleased(e -> keysPressed.remove(e.getCode()));
                canvasElement.requestFocus();
            }
        });

        canvasElement.setOnMouseClicked(event -> {
            double mapX = event.getX() + gameState.getOffsetX();
            double mapY = event.getY() + gameState.getOffsetY();
            String message = gameState.handleMouseClick(mapX, mapY);
            consoleLabel.setText(message);
        });

        setupAnimationLoop();
    }

    private void handleKeys() {
        double dx = 0;
        double dy = 0;

        if (keysPressed.contains(KeyCode.W)) {
            dy -= SCROLL_SPEED;
        }
        if (keysPressed.contains(KeyCode.S)) {
            dy += SCROLL_SPEED;
        }
        if (keysPressed.contains(KeyCode.A)) {
            dx -= SCROLL_SPEED;
        }
        if (keysPressed.contains(KeyCode.D)) {
            dx += SCROLL_SPEED;
        }

        if (dx != 0 || dy != 0) {
            moveMap(dx, dy);
        }
    }

    private void setupAnimationLoop() {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                handleKeys();

                long currentTime = System.currentTimeMillis();

                if (currentTime - gameState.getLastVehicleGenerationTime() >= VEHICLE_GENERATION_INTERVAL) {
                    gameState.getVehicleManager().generateVehicle();
                    gameState.setLastVehicleGenerationTime(currentTime);
                }

                if (currentTime - gameState.getLastIncidentGenerationTime() >= INCIDENT_GENERATION_INTERVAL) {
                    gameState.getIncidentManager().generateIncident();
                    gameState.setLastIncidentGenerationTime(currentTime);
                }

                gameState.getVehicleManager().updateVehicles();

                GraphicsContext gc = canvasElement.getGraphicsContext2D();
                gc.clearRect(0, 0, canvasElement.getWidth(), canvasElement.getHeight());

                double offsetX = gameState.getOffsetX();
                double offsetY = gameState.getOffsetY();

                gameState.getTrafficMap().draw(gc, offsetX, offsetY);
                gameState.getVehicleManager().draw(gc, offsetX, offsetY);
                gameState.getIncidentManager().draw(gc, offsetX, offsetY);
            }
        };
    }

    public void startAnimation() {
        if (animationTimer != null) {
            animationTimer.start();
        }
    }
    private void moveMap(double dx, double dy) {
        double newOffsetX = gameState.getOffsetX() + dx;
        double newOffsetY = gameState.getOffsetY() + dy;
        double canvasWidth = canvasElement.getWidth();
        double canvasHeight = canvasElement.getHeight();
        double mapPixelWidth = gameState.getTrafficMap().getWidth();
        double mapPixelHeight = gameState.getTrafficMap().getHeight();
        if (mapPixelWidth <= canvasWidth) {
            newOffsetX = 0;
        } else {
            newOffsetX = Math.max(0, Math.min(newOffsetX, mapPixelWidth - canvasWidth));
        }

        if (mapPixelHeight <= canvasHeight) {
            newOffsetY = 0;
        } else {
            newOffsetY = Math.max(0, Math.min(newOffsetY, mapPixelHeight - canvasHeight));
        }

        gameState.setOffsetX(newOffsetX);
        gameState.setOffsetY(newOffsetY);
    }
}
