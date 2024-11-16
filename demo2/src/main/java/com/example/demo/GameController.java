package com.example.demo;

import java.io.IOException;
import java.util.List;
import java.util.Set;

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

/**
 * this class represents the gameplay screen of the sightreading app game
 * 
 * @author Lucas Arsenault
 * @author 
 */
public class GameController {

    private MidiInputProcessor inputProcessor;
    private MeasureGenerator measureGenerator;

    private int minComplexity;
    private int maxComplexity;
    private Set<Integer> currentNotes;
    private List<Chord> currentMeasure;
    private int currentBeat;
    private boolean isCorrect;

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
        this.inputProcessor = new MidiInputProcessor();
        this.isCorrect = true;
        newMeasure();
        startCountDown();
        // while (timeRemaining > 0) {
        //     checkAnswer();
        // }
    }

    private void startCountDown(){
        countdownTimer = new Timeline(new KeyFrame(
            Duration.seconds(1),
            event -> {
                timeRemaining--;
                int minutes = timeRemaining / 60;
                int seconds = timeRemaining % 60;
                checkForInput();
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
        chordLabel.setText(currentMeasure.get(currentBeat).getNotes().toString());
    }

    private boolean measureComplete(){
        return currentBeat == currentMeasure.size();
    }

    /**
     * Compares the currently input set of notes to
     * the correct set of notes for a single beat in
     * a measure. If the measure is complete and
     * the every beat input was correct, the score increments.
     */
    private boolean checkChord(){
        
        if(currentNotes.equals(currentMeasure.get(currentBeat).getNotes())){
            currentBeat++;
            return true;
        }
        return false;
    }


    public  void checkForInput(){
        while(timeRemaining >= 0){
            checkAnswer();
        }
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

    /**
     * Returns the input from the user using a MIDI keyboard.
     * 
     * @return The set of integers representing notes pressed.
     */
    private Set<Integer> getInput() {
        return inputProcessor.getInputs();
    }

    /**
     * Checks the answer provided for a single beat
     * to the correct answer for that single beat.
     */
    private void checkAnswer() {
        currentNotes = getInput();
        System.out.println("InputProcessor:  "   + currentNotes.toString());
        if (isCorrect) {
            isCorrect = checkChord();
        } else {
            checkChord();
        }

        if (measureComplete()) {
            if (isCorrect) {
                score++;
            }
            newMeasure();
        }
    }

}
