package com.example.demo;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("clock.fxml"));
        Parent root = fxmlLoader.load();
        CountDownController controller = fxmlLoader.getController();
        controller.setStage(stage);
        stage.setOnShown(event -> controller.startCountdown());
        Scene scene = new Scene(root, 600, 350);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}