package model;

import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.Set;

/**
 * Takes in MIDI keyboard input, and translates it
 * to sound and a <set of> letter name values corresponding
 * to the musical note(s) that have been played.
 * 
 * @author Lucas Arsenault
 * @author Jean Michel Mugabe
 */
public class MidiInputProcessor {

    /**
     * Synthesizer generates sound.
     */
    private Synthesizer synthesizer;

    /**
     * Channel for sending and receiving MIDI messages.
     */
    private MidiChannel channel;

    /**
     * Receiver for MIDI keyboard input.
     */
    private MidiInputReceiver receiver;

    /**
     * Device for MIDI keyboard.
     */
    private MidiDevice device;

    /**
     * Constructor for a MidiInputProcessor object.
     * Initializes the device, receiver, and synthesizer.
     */
    public MidiInputProcessor() {
        // Initialize MIDI device
        MidiDevice.Info[] deviceInfo = MidiSystem.getMidiDeviceInfo();
        for(int i=0; i< deviceInfo.length;i++){
            try {
                device = MidiSystem.getMidiDevice(deviceInfo[i]);
                if (!device.isOpen()){
                    device.open();
                }
                System.out.println("Successfully connected to: " + deviceInfo[i]);
                
                // Set the MIDI input reciever to get MIDI data from the device 
                receiver = new MidiInputReceiver(device.getDeviceInfo().toString(), channel);
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

    /**
     * Returns a list of notes currently being played as part of a chord.
     * 
     * @return List of notes currently played.
     */
    public Set<Integer> getInputs() {
        return receiver.getNoteNames();
    }

    /**
     * Returns the MIDI device.
     * 
     * @return MIDI device.
     */
    public MidiDevice getDevice() {
        return device;
    }
    
}