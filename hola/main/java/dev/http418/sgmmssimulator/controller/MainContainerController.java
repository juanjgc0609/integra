package dev.http418.sgmmssimulator.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainContainerController implements Initializable {

    @FXML private StackPane sceneContainer;
    @FXML private ToggleGroup switchSceneGroup;
    @FXML private Label clockLabel;
    @FXML private Label sceneTitleLabel;

    private Parent trafficMapRoot;
    private Parent monitoringCenterRoot;
    private Parent incidentPanelRoot;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startClockTimer();
        preloadScenes();
        setupListeners();
        loadScene("monitoring-center-view");
    }

    private void setupListeners() {
        switchSceneGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                String selectedScene = newToggle.getUserData().toString();
                loadScene(selectedScene);
                changeSceneTitle(selectedScene);
            }
        });

        for (Toggle toggle : switchSceneGroup.getToggles()) {
            if (toggle instanceof RadioButton radioButton && radioButton.getGraphic() instanceof ImageView imageView) {
                Image originalImage = imageView.getImage();
                WritableImage invertedImage = new WritableImage((int) originalImage.getWidth(), (int) originalImage.getHeight());
                PixelReader reader = originalImage.getPixelReader();
                PixelWriter writer = invertedImage.getPixelWriter();

                for (int y = 0; y < originalImage.getHeight(); y++) {
                    for (int x = 0; x < originalImage.getWidth(); x++) {
                        Color color = reader.getColor(x, y);
                        writer.setColor(x, y, color.invert());
                    }
                }
                imageView.setImage(invertedImage);
            }
        }
    }
    private void preloadScenes() {
        try {
            // Mapa de tráfico
            FXMLLoader trafficMapLoader = new FXMLLoader(getClass().getResource("/dev/http418/sgmmssimulator/views/traffic-map-view.fxml"));
            trafficMapRoot = trafficMapLoader.load();
            TrafficMapController trafficMapController = trafficMapLoader.getController();
            trafficMapController.startAnimation(); // 👈 Se inicia solo una vez

            // Centro de monitoreo
            FXMLLoader monitorLoader = new FXMLLoader(getClass().getResource("/dev/http418/sgmmssimulator/views/monitoring-center-view.fxml"));
            monitoringCenterRoot = monitorLoader.load();

            // Panel de incidentes
            FXMLLoader incidentLoader = new FXMLLoader(getClass().getResource("/dev/http418/sgmmssimulator/views/incident-panel-view.fxml"));
            incidentPanelRoot = incidentLoader.load();

        } catch (IOException e) {
            showError("Error al cargar las vistas", e.getMessage());
            e.printStackTrace();
        }
    }
    private void loadScene(String scene) {
        switch (scene) {
            case "traffic-map-view" -> sceneContainer.getChildren().setAll(trafficMapRoot);
            case "monitoring-center-view" -> sceneContainer.getChildren().setAll(monitoringCenterRoot);
            case "incident-panel-view" -> sceneContainer.getChildren().setAll(incidentPanelRoot);
        }
        changeSceneTitle(scene);
    }
    private void changeSceneTitle(String scene) {
        String title = switch (scene) {
            case "traffic-map-view" -> "Mapa de tráfico";
            case "monitoring-center-view" -> "Centro de monitoreo";
            case "incident-panel-view" -> "Panel de incidentes";
            default -> "";
        };
        sceneTitleLabel.setText(title);
    }


    private void startClockTimer() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    javafx.application.Platform.runLater(() -> {
                        clockLabel.setText(java.time.LocalTime.now().withNano(0).toString());
                    });
                } catch (InterruptedException ignored) {}
            }
        }).start();
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
