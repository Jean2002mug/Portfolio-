package modelTest;

/**
 * @author Jean Michel Mugabe
*/

import model.MidiSetup;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MidiSetupTest {

    private MidiSetup midiSetup;

    @Before
    public void setUp() {
        // Initialize the MidiSetup instance before each test
        midiSetup = new MidiSetup();
    }

    @Test
    public void testMidiConnectionSetup() {
        // Test that the keyboardSetup flag is set correctly
        boolean isSetup = midiSetup.isKeyboardSetup();
        assertTrue("The keyboard should be set up if a valid device is found.", isSetup);
    }

    @Test
    public void testDeviceName() {
        // Ensure that the device name is not null or empty after setup
        assertNotNull("Device name should not be null after setup.", midiSetup.getDeviceName());
        assertFalse("Device name should not be empty after setup.", midiSetup.getDeviceName().isEmpty());
    }
}

  