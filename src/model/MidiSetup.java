package model;

/*
 * This class is responsible for setting up MIDI- keyboard.
 * It is responsible for detecting notes played on the keyboard and 
 * sending the signal that the keyboard. 
 *  @author : Jean Michel Mugabe
 * 
 */
import javax.sound.midi.*;
import midi.MidiInputReceiver;

public class MidiSetup {
    private MidiInputReceiver midiInputReceiver;
    private Boolean keyboardSetup = false; // Initialize to false
    private MidiDevice midiDevice;
    private Synthesizer synthesizer;
    private MidiChannel channel;
    private String deviceName;

    public MidiSetup() {
        this.channel = initializeSynthesizer();
        selectMIDI();
        keyboardSetup();
    }

    private MidiChannel initializeSynthesizer() {
        try {
            synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
            return synthesizer.getChannels()[0]; // Get the first MIDI channel
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void selectMIDI() {
        MidiDevice.Info[] deviceInfo = MidiSystem.getMidiDeviceInfo();
        for (MidiDevice.Info info : deviceInfo) {
            try {
                midiDevice = MidiSystem.getMidiDevice(info);
                openMidiConnection(midiDevice);
                deviceName = midiDevice.getDeviceInfo().toString();

                // Set the MIDI input receiver to get MIDI data from the device
                midiDevice.getTransmitter().setReceiver(new MidiInputReceiver(deviceName, channel));
                keyboardSetup = true;
                break; // Stop after finding the first available device
            } catch (MidiUnavailableException e) {
                System.out.println("Failed to open MIDI device: " + info.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void openMidiConnection(MidiDevice device) throws MidiUnavailableException {
        if (!device.isOpen()) {
            device.open();
        }
    }

    public void closeMidiConnection() {
        if (midiDevice != null && midiDevice.isOpen()) {
            midiDevice.close();
        }
        if (synthesizer != null && synthesizer.isOpen()) {
            synthesizer.close();
        }
    }

    private boolean isKeyboardSetup() {
        return keyboardSetup;
    }
    public void keyboardSetup(){
        if(isKeyboardSetup()){
            System.out.println("MIDI device " + deviceName + " setup successfully.");
        }
        else{
            System.out.println("no keyboard has been connected");
        }
    }
    public static void main(String[] args) {
        new MidiSetup();
    }
}

