package midi;

import javax.sound.midi.*;

public class MidiInputReceiver implements Receiver {
    private String name;
    private MidiChannel channel;
    private boolean firstKeyPressed; // Track if the first key was pressed

    public MidiInputReceiver(String name, MidiChannel channel) {
        this.name = name;
        this.channel = channel;
        this.firstKeyPressed = false; // Initialize to false
    }

    @Override
    public void send(MidiMessage message, long timeStamp) {
        if (message instanceof ShortMessage) {
            ShortMessage sm = (ShortMessage) message;
            if (sm.getCommand() == ShortMessage.NOTE_ON) {
                int key = sm.getData1();
                int velocity = sm.getData2();

                if (velocity > 0) {
                    channel.noteOn(key, velocity); // Play the note
                    System.out.println("Note ON: " + key + " Velocity: " + velocity);
                    if (!firstKeyPressed) {
                        System.out.println("The keyboard is working!");
                        firstKeyPressed = true; // Set to true after first key press
                    }
                } else {
                    channel.noteOff(key); // Stop the note if velocity is 0
                }
            } else if (sm.getCommand() == ShortMessage.NOTE_OFF) {

                int key = sm.getData1();
                channel.noteOff(key); // Stop the note
                System.out.println("Note OFF: " + key + " Velocity: " );
            }
        }
    }

    @Override
    public void close() {
        // Implement close if needed for resource management
        System.out.println("Closing MidiInputReceiver");
    }
}