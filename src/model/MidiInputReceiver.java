package model;

import javax.sound.midi.*;
import java.util.ArrayList;

/**
 * Receives the MIDI message, and translates the raw
 * data into readable data, and sends the message.
 * 
 * @author Lucas Arsenault
 * @author Jean Michel Mugabe
 */
public class MidiInputReceiver implements Receiver {
    
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
    private ArrayList<String> noteNames;

    /**
     * Constructs an input receiver.
     * 
     * @param name The name of the input receiver.
     * @param channel The channel for sending and receiving MIDI messages.
     */
    public MidiInputReceiver(String name, MidiChannel channel) {
        this.channel = channel;
    }

    @Override
    public void send(MidiMessage message, long timeStamp) {
        if (message instanceof ShortMessage) {
            ShortMessage sm = (ShortMessage) message;
            int command = sm.getCommand();
            int key = sm.getData1();
            String noteName = getNoteName(key % NUM_NOTES);
            int velocity = sm.getData2();

            if (command == ShortMessage.NOTE_ON && velocity > 0) {
                System.out.println("Note ON: " + noteName + " | Velocity: " + velocity);
                noteNames.add(noteName);
                try {
                    channel.noteOn(key, velocity);
                } catch (NullPointerException e) {}
            } else if (command == ShortMessage.NOTE_OFF || (command == ShortMessage.NOTE_ON && velocity == 0)) {
                System.out.println("Note OFF: " + noteName);
                noteNames.remove(noteName);
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

    /**
     * Returns the list of note names for the input currently being played.
     * 
     * @return List of note names for the input currently being played.
     */
    public ArrayList<String> getNoteNames() {
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
    private String getNoteName(int key) {
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
}