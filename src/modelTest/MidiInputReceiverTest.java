package modelTest;

/**
 * @author Jean Michel Mugabe
*/

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiChannel;
import model.MidiInputReceiver;

import java.util.ArrayList;

public class MidiInputReceiverTest {
    private MidiInputReceiver midiInputReceiver;
    private MidiChannel channel;
    private Synthesizer synthesizer;

    @Before
    public void setUp() throws MidiUnavailableException {
        // Get the real synthesizer
        synthesizer = MidiSystem.getSynthesizer();
        synthesizer.open();
        
        // Get the first available channel
        channel = synthesizer.getChannels()[0];
        if (channel == null) {
            throw new MidiUnavailableException("No MIDI channels available");
        }
        
        midiInputReceiver = new MidiInputReceiver("TestReceiver", channel);
    }

    @Test
    public void testSendNoteOnMessage() throws Exception {
        // Create a ShortMessage for NOTE_ON with a velocity > 0
        ShortMessage noteOnMessage = new ShortMessage(ShortMessage.NOTE_ON, 0, 60, 100); // MIDI key 60 (Middle C), velocity 100
        midiInputReceiver.send(noteOnMessage, -1);

        // Check if the note name "C" was added to the noteNames list
        ArrayList<String> noteNames = midiInputReceiver.getNoteNames();
        assertTrue("Note name should be in the list", noteNames.contains("C"));
        
        // Small delay to allow the note to be heard if needed
        Thread.sleep(100);
    }

    @Test
    public void testSendNoteOffMessage() throws Exception {
        // Create a ShortMessage for NOTE_ON followed by NOTE_OFF
        ShortMessage noteOnMessage = new ShortMessage(ShortMessage.NOTE_ON, 0, 60, 100);
        ShortMessage noteOffMessage = new ShortMessage(ShortMessage.NOTE_OFF, 0, 60, 0);

        midiInputReceiver.send(noteOnMessage, -1); // Simulate note ON
        Thread.sleep(100); // Small delay between on and off
        midiInputReceiver.send(noteOffMessage, -1); // Simulate note OFF

        // Check if the note name "C" was removed from the noteNames list
        ArrayList<String> noteNames = midiInputReceiver.getNoteNames();
        assertFalse("Note name should be removed from the list", noteNames.contains("C"));
    }

    @org.junit.After
    public void tearDown() {
        if (synthesizer != null && synthesizer.isOpen()) {
            synthesizer.close();
        }
    }
}