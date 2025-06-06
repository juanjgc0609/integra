package dev.http418.sgmmssimulator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SGMMSApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SGMMSApp.class.getResource("views/main-container-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Image icon = new Image(Objects.requireNonNull(SGMMSApp.class.getResourceAsStream("icons/Palmera.png")));
        stage.getIcons().add(icon);
        stage.setTitle("SGMMS - Sistema de Gestión y Monitoreo de Movilidad y Seguridad");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}