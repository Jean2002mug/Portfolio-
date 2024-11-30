package com.example.demo;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.MeasureGenerator;

import java.io.IOException;

public class LevelSelectPageController {
    Stage stage;

    public void setStage(Stage stage){
        this.stage = stage;
    }


    public void levelOne(){
        moveToNextPage(0, 0, new MeasureGenerator(1, 11, 1,4));
    }

    public void levelTwo(){
        moveToNextPage(1,1,new MeasureGenerator(1, 11, 1,4));
    }



    private void moveToNextPage(int minComplexity, int maxComplexity, MeasureGenerator generator){
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
        } catch (IOException e){

        }
    }


}
