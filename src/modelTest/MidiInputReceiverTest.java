package modelTest;

import model.MidiInputReceiver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.sound.midi.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class MidiInputReceiverTest {

    private MidiChannel mockChannel;
    private MidiInputReceiver midiInputReceiver;

    @BeforeEach
    public void setUp() {
        mockChannel = mock(MidiChannel.class);
        midiInputReceiver = new MidiInputReceiver("TestReceiver", mockChannel);
    }

    @Test
    public void testSend_NoteOn() throws InvalidMidiDataException {
        // Simulate sending a NOTE_ON message with velocity > 0
        ShortMessage noteOnMessage = new ShortMessage(ShortMessage.NOTE_ON, 0, 60, 100);
        midiInputReceiver.send(noteOnMessage, -1);

        // Verify that noteOn was called on the channel with the correct key and velocity
        verify(mockChannel).noteOn(60, 100);
    }

    @Test
    public void testSend_NoteOff() throws InvalidMidiDataException {
        // Simulate sending a NOTE_OFF message
        ShortMessage noteOffMessage = new ShortMessage(ShortMessage.NOTE_OFF, 0, 60, 0);
        midiInputReceiver.send(noteOffMessage, -1);

        // Verify that noteOff was called on the channel with the correct key
        verify(mockChannel).noteOff(60);
    }

    @Test
    public void testSend_NoteOnWithZeroVelocity() throws InvalidMidiDataException {
        // Simulate sending a NOTE_ON message with zero velocity (equivalent to NOTE_OFF)
        ShortMessage noteOnZeroVelocity = new ShortMessage(ShortMessage.NOTE_ON, 0, 60, 0);
        midiInputReceiver.send(noteOnZeroVelocity, -1);

        // Verify that noteOff was called due to zero velocity
        verify(mockChannel).noteOff(60);
    }

    @Test
    public void testGetNoteName() {
        // Test some key-to-note translations
        assertEquals("C", midiInputReceiver.getNoteName(11));
        assertEquals("C#", midiInputReceiver.getNoteName(0));
        assertEquals("D", midiInputReceiver.getNoteName(1));
        assertEquals("E", midiInputReceiver.getNoteName(3));
        assertEquals("A", midiInputReceiver.getNoteName(8));
    }
}