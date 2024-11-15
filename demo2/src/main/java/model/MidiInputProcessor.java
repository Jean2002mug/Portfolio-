package model;
import javax.sound.midi.*;
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
    private String  midiDevice;

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
                midiDevice= deviceInfo[i].getName();
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
    public String getDevice(){
        return midiDevice;
    }
    public static void main(String[] args) {
        new MidiInputProcessor();
    }
    
}