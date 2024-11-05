package midi;

import javax.sound.midi.*;

public class MidiInputReceiver implements Receiver {
    private String name;
    private MidiChannel channel;

    public MidiInputReceiver(String name, MidiChannel channel) {
        this.name = name;
        this.channel = channel;
    }

    @Override
    public void send(MidiMessage message, long timeStamp) {
        if (message instanceof ShortMessage) {
            ShortMessage sm = (ShortMessage) message;
            int command = sm.getCommand();
            int key = sm.getData1(); // Note value (MIDI number)
            int velocity = sm.getData2(); // Velocity value

            if (command == ShortMessage.NOTE_ON && velocity > 0) {
                System.out.println("Note ON: " + key + " Velocity: " + velocity);
                channel.noteOn(key, velocity); // Play the note with velocity
            } else if (command == ShortMessage.NOTE_OFF || (command == ShortMessage.NOTE_ON && velocity == 0)) {
                System.out.println("Note OFF: " + key);
                channel.noteOff(key); // Stop the note
            }
        }
    }

    @Override
    public void close() {
        // Implement close if needed for resource management
        System.out.println("Closing MidiInputReceiver");
    }
}
