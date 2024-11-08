package model;

/*
 * This class is responsible for setting up MIDI- keyboard.
 * It is responsible for detecting notes played on the keyboard and 
 * sending the signal that the keyboard. 
 *  @author : Jean Michel Mugabe
 * 
 */
import javax.sound.midi.*;

public class MidiSetup {
    private Boolean keyboardSetup = false; // Initialize to false
    private MidiDevice midiDevice;
    private Synthesizer synthesizer;
    private MidiChannel channel;
    private String deviceName;
    private MidiInputProcessor midiInputProcessor;

    public MidiSetup() {
        midiInputProcessor= new MidiInputProcessor();
        openMidiConnection(midiInputProcessor);
        keyboardSetup();
    }

    private void openMidiConnection(MidiInputProcessor midiInputProcessor)  {

        deviceName= midiInputProcessor.getDevice().toString();
        if (deviceName.equals("Real Time Sequencer")) {
            keyboardSetup = true;
        }
    }
    

    private boolean isKeyboardSetup() {
        return keyboardSetup;
    }

    public void keyboardSetup() {
        if (isKeyboardSetup()) {
            System.out.println("MIDI device " + deviceName + " setup successfully.");
        } else {
            System.out.println("No keyboard has been connected");
        }
    }

    public static void main(String[] args) {
        new MidiSetup();
    }
}