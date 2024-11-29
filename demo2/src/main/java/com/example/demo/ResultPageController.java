package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class ResultPageController {
    @FXML
    Label scoreLabel;

    Stage stage;


    @FXML
    public void setScoreLabel(int score){
        scoreLabel.setText(Integer.toString(score));
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void scoreLabel(int score) {
        scoreLabel.setText(Integer.toString(score));
    }

    public void moveToNextPage(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("level-select-page.fxml"));
            Parent root = loader.load();
            LevelSelectPageController controller = loader.getController();
            controller.setStage(stage);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e){

        }
    }
}
