module dev.http418.sgmmssimulator {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens dev.http418.sgmmssimulator to javafx.fxml;
    exports dev.http418.sgmmssimulator;
    exports dev.http418.sgmmssimulator.controller;
    opens dev.http418.sgmmssimulator.controller to javafx.fxml;
    exports dev.http418.sgmmssimulator.util;
    opens dev.http418.sgmmssimulator.util to javafx.fxml;
}