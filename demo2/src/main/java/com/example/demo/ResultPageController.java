package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ResultPageController {
    @FXML
    Label scoreLabel;

    @FXML
    public void setScoreLabel(int score){
        scoreLabel.setText(Integer.toString(score));
    }

    public void scoreLabel(int score) {
        scoreLabel.setText(Integer.toString(score));
    }
}
