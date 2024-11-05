package model;
import javax.sound.midi.*;

/**
 * This class takes MIDI keyboard input, and
 * using java's built in sound library, outputs
 * a sound corresponding to the note(s) that were
 * pressed.
 * 
 * @author Lucas Arsenault
 * Last Updated: 2024-11-03
 */
public class MIDIInputProcessor {

    public static void main(String[] args) {
        // Get the MIDI synthesizer and open it
        Synthesizer synthesizer = MidiSystem.getSynthesizer();
        synthesizer.open();

        // Get the MIDI channel
        MidiChannel[] channels = synthesizer.getChannels();
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();

        // Find a MIDI input device and open it
        MidiDevice midiDevice = null;
        for (int i = 0; i < infos.length; i++) {
            MidiDevice.Info info = infos[i];
            if (info.getDescription().contains("MIDI")) {
                midiDevice = MidiSystem.getMidiDevice(info);
                if (!midiDevice.isOpen()) {
                    midiDevice.open();
                }
                break;
            }
        }

        // Set up a listener for MIDI events
        midiDevice.getTransmitter().setReceiver(new Receiver() {
            @Override
            public void send(MidiMessage message, long timeStamp) {
                // Check for note on message
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
                    if (sm.getCommand() == ShortMessage.NOTE_ON) {
                        int note = sm.getData1();
                        int velocity = sm.getData2();
                        System.out.println("Note On: " + note + ", Velocity: " + velocity);
                        // Play note
                        channels[0].noteOn(note, velocity);
                    } else if (sm.getCommand() == ShortMessage.NOTE_OFF) {
                        int note = sm.getData1();
                        System.out.println("Note Off: " + note);
                        // Stop note
                        channels[0].noteOff(note);
                    }
                }
            }

            @Override
            public void close() {}
        });

        // Keep program running
        System.out.println("Listening for MIDI input...");
        boolean running = true;
        while (running) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                running = false;
            }
        }
    }
}