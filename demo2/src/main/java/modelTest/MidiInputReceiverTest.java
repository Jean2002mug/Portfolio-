package modelTest;

/**
 * @author Jean Michel Mugabe
*/

import model.MidiInputReceiver;
import org.junit.Before;
import org.junit.Test;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.ShortMessage;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class MidiInputReceiverTest {

    private MidiChannel channel;
    private MidiInputReceiver midiInputReceiver;

    @Before
    public void setUp() {
        // Create a simple instance of MidiChannel; use anonymous class to avoid mocks
        channel = new MidiChannel() {
            private ArrayList<Integer> activeNotes = new ArrayList<>();

            @Override
            public void noteOn(int noteNumber, int velocity) {
                activeNotes.add(noteNumber);
            }

            @Override
            public void noteOff(int noteNumber, int velocity) {

            }

            @Override
            public void noteOff(int noteNumber) {
                activeNotes.remove((Integer) noteNumber);
            }

            // The rest of the MidiChannel methods can be left unimplemented if not needed
            @Override public void setPolyPressure(int noteNumber, int pressure) {}
            @Override public int getPolyPressure(int noteNumber) { return 0; }
            @Override public void setChannelPressure(int pressure) {}
            @Override public int getChannelPressure() { return 0; }
            @Override public void controlChange(int controller, int value) {}
            @Override public int getController(int controller) { return 0; }
            @Override public void programChange(int program) {}
            @Override public void programChange(int bank, int program) {}
            @Override public int getProgram() { return 0; }
            @Override public void setPitchBend(int bend) {}
            @Override public int getPitchBend() { return 0; }
            @Override public void resetAllControllers() {}
            @Override public void allNotesOff() { activeNotes.clear(); }
            @Override public void allSoundOff() { activeNotes.clear(); }
            @Override public boolean localControl(boolean on) { return false; }
            @Override public void setMono(boolean on) {}
            @Override public boolean getMono() { return false; }
            @Override public void setOmni(boolean on) {}
            @Override public boolean getOmni() { return false; }
            @Override public void setMute(boolean mute) {}
            @Override public boolean getMute() { return false; }
            @Override public void setSolo(boolean soloState) {}
            @Override public boolean getSolo() { return false; }
        };

        midiInputReceiver = new MidiInputReceiver("TestReceiver", channel);
    }

    @Test
    public void testSend_NoteOn() throws Exception {
        // Send a NOTE_ON message with velocity > 0
        ShortMessage noteOnMessage = new ShortMessage(ShortMessage.NOTE_ON, 0, 60, 100);
        midiInputReceiver.send(noteOnMessage, 2);

        // Verify that the note name is added to the list
        ArrayList<String> noteNames = midiInputReceiver.getNoteNames();
        assertTrue("Note name should be present when a note is played.", noteNames.contains("C"));
    }

    @Test
    public void testSend_NoteOff() throws Exception {
        // Send a NOTE_ON message to add the note first
        ShortMessage noteOnMessage = new ShortMessage(ShortMessage.NOTE_ON, 0, 60, 100);
        midiInputReceiver.send(noteOnMessage, -1);

        // Send a NOTE_OFF message to remove the note
        ShortMessage noteOffMessage = new ShortMessage(ShortMessage.NOTE_OFF, 0, 60, 0);
        midiInputReceiver.send(noteOffMessage, -1);

        // Verify that the note name is removed from the list
        ArrayList<String> noteNames = midiInputReceiver.getNoteNames();
        assertFalse("Note name should be removed when the note is turned off.", noteNames.contains("C"));
    }

    @Test
    public void testGetNoteName() {
        // Test the translation of key values to note names
        assertEquals("C", midiInputReceiver.getNoteName(11));
        assertEquals("C#", midiInputReceiver.getNoteName(0));
        assertEquals("D", midiInputReceiver.getNoteName(1));
        assertEquals("E", midiInputReceiver.getNoteName(3));
        assertEquals("A", midiInputReceiver.getNoteName(8));
    }
}