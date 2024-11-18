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
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("game-core-view.fxml"));
        Parent root = fxmlLoader.load();
        GameController controller = fxmlLoader.getController();
        controller.setStage(stage);
        Scene scene = new Scene(root, 1200, 580);
        stage.setTitle("Sight-reading App");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}