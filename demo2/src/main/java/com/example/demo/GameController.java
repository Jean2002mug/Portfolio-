package com.example.demo;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Chord;
import model.MeasureGenerator;
import model.MidiInputProcessor;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @author this class represents the gameplay screen of the sightreading app game
 */
public class GameController {

    private MidiInputProcessor inputProcessor;
    private MeasureGenerator measureGenerator;

    private int minComplexity;
    private int maxComplexity;
    private Set<Integer> currentNotes;
    private List<Chord> currentMeasure;
    private int currentBeat;

    private int timeRemaining;
    private int score;

    private Timeline countdownTimer;

    private Stage stage;

    @FXML
    private Label countDownLabel;

    @FXML
    private Label chordLabel;

    @FXML
    public void setStage(Stage stage){
        this.stage = stage;
    }

    @FXML
    public void initialize(MeasureGenerator measureGenerator, int minComplexity, int maxComplexity, int timeRemaining){
        this.minComplexity = minComplexity;
        this.maxComplexity = maxComplexity;
        this.timeRemaining = timeRemaining;
        currentBeat = 0;
        this.measureGenerator = measureGenerator;
        this.score = 0;
        newMeasure();
        startCountDown();
    }

    private void startCountDown(){
        countdownTimer = new Timeline(new KeyFrame(
                Duration.seconds(1),
                event -> {
                    timeRemaining--;
                    int minutes = timeRemaining / 60;
                    int seconds = timeRemaining % 60;
                    countDownLabel.setText(Integer.toString(minutes) + ":" + Integer.toString(seconds));
                            if (timeRemaining <= 0) {
                                countdownTimer.stop();
                                moveToNextPage();
                            }
                        }
                ));

                countdownTimer.setCycleCount(Timeline.INDEFINITE);
                countdownTimer.play();
    }


    private void newMeasure(){
        this.currentMeasure = this.measureGenerator.nextMeasure(minComplexity, maxComplexity);
        chordLabel.setText(currentMeasure.get(currentBeat).toString());
    }

    private boolean measureComplete(){
        return currentBeat == currentMeasure.size();
    }

    private boolean correctChord(){
        if(currentNotes.equals(currentMeasure.get(currentBeat).getNotes())){
            currentBeat++;
            this.score++;
            return true;
        }
        return false;
    }

    private void  moveToNextPage(){
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("result-page.fxml"));
            Parent root = loader.load();

            ResultPageController controller = loader.getController();
            controller.scoreLabel(this.score);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e){

        }

    }



}
