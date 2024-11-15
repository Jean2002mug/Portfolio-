package modelTest;

/**
 * @author Jean Michel Mugabe
*/

import model.MidiInputProcessor;
import org.junit.Before;
import org.junit.Test;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import static org.junit.Assert.*;

public class MidiInputProcessorTest {

    private MidiInputProcessor midiInputProcessor;

    @Before
    public void setUp() {
        // Create an instance of MidiInputProcessor for testing
        midiInputProcessor = new MidiInputProcessor();
    }

    @Test
    public void testSynthesizerInitialization() {
        // Verify that the synthesizer has been initialized
        try {
            Synthesizer synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
            assertNotNull("Synthesizer should be initialized", synthesizer);
            assertTrue("Synthesizer should be open", synthesizer.isOpen());
            synthesizer.close();
        } catch (MidiUnavailableException e) {
            fail("Synthesizer initialization failed: " + e.getMessage());
        }
    }

    @Test
    public void testMidiDeviceConnection() {
        // Check if the MIDI device name is not null after initialization
        String deviceName = midiInputProcessor.getDevice();
        assertNotNull("MIDI device name should not be null", deviceName);
        assertFalse("MIDI device name should not be empty", deviceName.isEmpty());
    }

    @Test
    public void testMidiChannelInitialization() {
        // Verify that the MIDI channel has been initialized
        try {
            Synthesizer synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
            assertNotNull("MIDI channel should be initialized", synthesizer.getChannels()[0]);
            synthesizer.close();
        } catch (MidiUnavailableException e) {
            fail("MIDI channel initialization failed: " + e.getMessage());
        }
    }

    @Test
    public void testMultipleDevicesHandled() {
        // Verify that the class handles multiple MIDI devices
        boolean multipleDevices = MidiSystem.getMidiDeviceInfo().length > 1;
        assertTrue("There should be more than one MIDI device available", multipleDevices);
    }

    @Test
    public void testExceptionHandlingForUnavailableDevice() {
        // Verify that exceptions are handled properly
        try {
            new MidiInputProcessor();
        } catch (Exception e) {
            fail("Initialization should handle exceptions gracefully without failing: " + e.getMessage());
        }
    }
}