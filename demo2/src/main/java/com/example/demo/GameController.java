package com.example.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Chord;
import model.MeasureGenerator;

/**
 * this class represents the gameplay screen of the sightreading app game
 * 
 * @author Lucas Arsenault
 * @author 
 */
public class GameController {

    private MidiInputReceiver receiver;
    private MidiDevice device ;
    private MeasureGenerator measureGenerator;

    private int minComplexity;
    private int maxComplexity;
    private Set<Integer> currentNotes = new HashSet<>();
    public List<Chord> currentMeasure =new ArrayList<>();
    private int currentBeat;
    private boolean isCorrect;

    public int timeRemaining;
    private int score;

    public Timeline countdownTimer;
    private Timeline inputCheckTimeline;
    private Stage stage;

    @FXML
    private Label countDownLabel;

    @FXML
    private Label chordLabel;

    @FXML
    private Label scoreLabel;

    @FXML
    public void setStage(Stage stage){
        this.stage = stage;
    }

    @FXML
    private ImageView noteView;

    @FXML
    private ImageView feedbackView;


    private void startCountDown(){
        countdownTimer = new Timeline(new KeyFrame(
            Duration.seconds(1),
            event -> {
                timeRemaining--;
                int minutes = timeRemaining / 60;
                int seconds = timeRemaining % 60;
                //checkForInput();
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
        Platform.runLater(()->{
            String note = currentMeasure.get(0).getRootNoteName();
            Image image = new Image(getClass().getResource("/noteImages/" + note + ".png").toExternalForm());
            noteView.setImage(image);
        });
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
   @FXML
   public void initialize(){
        this.minComplexity = 0;
        this.maxComplexity = 0;
    //     this.timeRemaining = 200;
        currentBeat = 0;
        this.measureGenerator =  new MeasureGenerator(1, 11, 1,4);
        this.score = 0;
    //     System.out.println("Game Controller" + stage.getScene().getRoot());
    //     this.isCorrect = true;
    //     currentNotes = new HashSet<>();
           newMeasure();
    //     Platform.runLater( () ->{
    //         Parent root = stage.getScene().getRoot();
    //     root.addEventHandler(NoteEvent.NOTE_PLAYED, event -> {
    //         int noteKey = event.getNoteKey();
    //         int velocity = event.getVelocity();
    //         System.out.println("Note played: Key " + noteKey + ", Velocity " + velocity);

    //         // Handle the note input (for example, check if it's correct)
    //         currentNotes.add(noteKey); // Add the note to the set of current notes
    //         checkAnswer(); // Check if the input matches the correct answer
    //     });

    // });
   
    setupMidi();
    

   }
    
    private void startInputChecking(){
        inputCheckTimeline= new Timeline(new KeyFrame( Duration.millis(100), event -> checkAnswer()));
        inputCheckTimeline.setCycleCount(Timeline.INDEFINITE);
        inputCheckTimeline.play();
    }

    private void incrementScore(){
        score++;
        Platform.runLater(() -> {
            scoreLabel.setText("Score: " + score);
        });
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
    // private Set<Integer> getInput() {
    //     return inputProcessor.getInputs();
    // }

    /**
     * Checks the answer provided for a single beat
     * to the correct answer for that single beat.
     */
    private void checkAnswer() {
        if (currentNotes == null || currentNotes.isEmpty()) {
            System.out.println("No input detected or currentNotes is null.");
            return;
        }
        // currentNotes = getInput();
        System.out.println("InputProcessor:  "   + currentNotes.toString());
        if (isCorrect) {
            isCorrect = checkChord();
        } 
        else {
             checkChord();
         }

        if (measureComplete()) {
            if (isCorrect) {
                feedbackView.setImage(new Image(getClass().getResource("/feedbackImages/correct.png").toExternalForm()));
                score++;
            } else {
                feedbackView.setImage(new Image(getClass().getResource("/feedbackImages/incorrect.png").toExternalForm()));
            }
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.play();
            feedbackView.setImage(null);
            newMeasure();
        }
    }
    private void setupMidi(){
        MidiDevice.Info[] deviceInfo = MidiSystem.getMidiDeviceInfo();
        for(int i=0; i< deviceInfo.length;i++){
            try {
                MidiDevice device = MidiSystem.getMidiDevice(deviceInfo[i]);
                if (!device.isOpen()){
                    device.open();
                }
                System.out.println("Successfully connected to: " + deviceInfo[i]);
                
                // Set the MIDI input reciever to get MIDI data from the device 
                Synthesizer synthesizer = MidiSystem.getSynthesizer();
                synthesizer.open();
                MidiChannel channel = synthesizer.getChannels()[0];
                receiver = new MidiInputReceiver(device.getDeviceInfo().toString(), channel);
                device.getTransmitter().setReceiver(receiver);
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    
private  class MidiInputReceiver implements Receiver {
    
    /**
     * Number of unique notes on the keyboard.
     */
    private static final int NUM_NOTES = 12;

    /**
     * Channel for sending and receiving MIDI messages.
     */
    private MidiChannel channel;

    /**
     * List of note names for the inputs currently played.
     */
    private Set<Integer> noteNames = new HashSet<>();

    private Node eventTarget;

    /**
     * Constructs an input receiver.
     * 
     * @param name The name of the input receiver.
     * @param channel The channel for sending and receiving MIDI messages.
     */
    public MidiInputReceiver(String name, MidiChannel channel) {
        this.channel = channel;
        this.eventTarget = eventTarget;
    }

   


    @Override
    public void send(MidiMessage message, long timeStamp) {
        if (message instanceof ShortMessage) {
            ShortMessage sm = (ShortMessage) message;
            // Case of pressing PB1 or PB2 for pitch bending. 
            if (sm.getCommand() >= 0xE0 && sm.getCommand() <= 0xEF) {// if the button pressed is PB1 or PB2 then ignore it 
                System.out.println("Pitch Bend message ignored");
                return;
            }

            if (sm.getCommand() == 0xB0) {// This ignores the pressing of PB1 or PB2 for any modification except pitch bending .
                int controllerNumber = sm.getData1(); // Controller number
                if (isPB1OrPB2Controller(controllerNumber)) {
                    System.out.println("Control Change message from PB1/PB2 ignored");
                    return;
                }
            }
            int command = sm.getCommand();
            int key = sm.getData1() % NUM_NOTES;
            String noteName = getNoteName(key);
            int velocity = sm.getData2();

            if (command == ShortMessage.NOTE_ON && velocity > 0) {
                System.out.println("Note ON: " + key + " | Velocity: " + velocity);
                currentNotes.add(key);
                if (currentNotes.equals(currentMeasure.get(0).getNotes())){
                    incrementScore();
                    newMeasure();
                } else {
                    newMeasure();
                }
  

               
                try {
                    channel.noteOn(key, velocity);
                } catch (NullPointerException e) {}
            } else if (command == ShortMessage.NOTE_OFF || (command == ShortMessage.NOTE_ON && velocity == 0)) {
                System.out.println("Note OFF: " + key);
                currentNotes.remove(key);
                
            
                try {
                    channel.noteOff(key);
                } catch (NullPointerException e) {}
            }
        }
    }

    @Override
    public void close() {
        // Implement close if needed for resource management
    }
    private boolean isPB1OrPB2Controller(int controllerNumber) {
        // PB1/PB2 controller numbers may vary; this decides whether the button pressed is PB1 or PB2.
        return controllerNumber == 1 || controllerNumber == 2; 
    }

    /**
     * Returns the list of note names for the input currently being played.
     * 
     * @return List of note names for the input currently being played.
     */
    public Set<Integer> getNoteNames() {
        return noteNames;
    }

    /**
     * Translates a MIDI key integer value to
     * a string containing the musical note name
     * for the corresponding key.
     * 
     * @param key The MIDI integer note value.
     * @return The corresponding musical note name.
     */
    public String getNoteName(int key) {
        String noteName = "";
        if (key == 11) {
            noteName = "C";
        } else if (key == 0) {
            noteName = "C#";
        } else if (key == 1) {
            noteName = "D";
        } else if (key == 2) {
            noteName = "D#";
        } else if (key == 3) {
            noteName = "E";
        } else if (key == 4) {
            noteName = "F";
        } else if (key == 5) {
            noteName = "F#";
        } else if (key == 6) {
            noteName = "G";
        } else if (key == 7) {
            noteName = "G#";
        } else if (key == 8) {
            noteName = "A";
        } else if (key == 9) {
            noteName = "A#";
        } else if (key == 10) {
            noteName = "B";
        }
        return noteName;
    }

    public void setNoteNames(Set<Integer> noteNames) {
      this.noteNames = noteNames;
    }

    public void setEventTarget(Node eventTarget) {
        this.eventTarget = eventTarget;
    }
}
}
