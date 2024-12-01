package com.example.demo;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.MeasureGenerator;

public class LevelDescriptionController {
    private Stage stage;
    private int minComplexity;
    private int maxComplexity;
    private MeasureGenerator generator;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Button startButton;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setLevelDetails(int minComplexity, int maxComplexity, String description) {
        this.minComplexity = minComplexity;
        this.maxComplexity = maxComplexity;
        this.generator = new MeasureGenerator(1, 11, 1, 4); // Example generator
        descriptionLabel.setText(description);
    }

    @FXML
    private void startLevel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("clock.fxml"));
            Parent root = loader.load();

            CountDownController controller = loader.getController();
            controller.setState(minComplexity, maxComplexity, generator);
            controller.setStage(stage);

            Scene scene = new Scene(root);
            controller.startCountdown();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
