package model;

import java.awt.event.*; 
import javax.sound.midi.*;
import javax.swing.*; 

/**
 * Takes in MIDI keyboard input, and translates it
 * to sound and a <set of> letter name values corresponding
 * to the musical note(s) that have been played.
 * 
 * @author Lucas Arsenault
 * @author Jean Michel Mugabe
 */
public class MidiInputProcessor extends JFrame implements KeyListener {

    /**
     * Synthesizer generates sound.
     */
    private Synthesizer synthesizer;

    /**
     * Channel for sending and receiving MIDI messages.
     */
    private MidiChannel channel;

    /**
     * Constructor for a MIDIInputProcessor object.
     * Initializes the device and synthesizer.
     */
    public MidiInputProcessor() {
        // Initialize MIDI device
        MidiDevice device;
        MidiDevice.Info[] deviceInfo= MidiSystem.getMidiDeviceInfo();
        for(int i=0; i< deviceInfo.length;i++){
            try {
                device = MidiSystem.getMidiDevice(deviceInfo[i]);
                if (!device.isOpen()){
                    device.open();
                }
                System.out.println("Successfully connected to: " + deviceInfo[i]);
                
                // Set the MIDI input reciever to get MIDI data from the device 
                MidiInputReceiver receiver = new MidiInputReceiver(device.getDeviceInfo().toString(), channel);
                device.getTransmitter().setReceiver(receiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Initialize MIDI synthesizer
        try {
            synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
            channel = synthesizer.getChannels()[0];
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Generate sound based on computer keyboard
        // int midiNote = mapKeyToMidi(e.getKeyCode());
        // if (midiNote != -1) {
        //     channel.noteOn(midiNote, 100); // Play note with velocity 100
        // }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // int midiNote = mapKeyToMidi(e.getKeyCode());
        // if (midiNote != -1) {
        //     channel.noteOff(midiNote); // Stop the note
        // }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // not implemented yet
    }

    public static void main(String[] args) {
        new MidiInputProcessor();
    }
    
}
