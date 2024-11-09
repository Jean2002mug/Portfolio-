package modelTest;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MidiSetupTest {

    private MidiSetup midiSetup;

    @BeforeEach
    public void setUp() {
        // Initialize the MidiSetup instance before each test
        midiSetup = new MidiSetup();
    }

    @Test
    public void testMidiConnectionSetup() {
        // Test that the keyboardSetup flag is set correctly
        boolean isSetup = midiSetup.isKeyboardSetup();
        assertTrue(isSetup, "The keyboard should be set up if a valid device is found.");
    }

    @Test
    public void testDeviceName() {
        // Ensure that the device name is not null or empty after setup
        assertNotNull(midiSetup.getDeviceName(), "Device name should not be null after setup.");
        assertFalse(midiSetup.getDeviceName().isEmpty(), "Device name should not be empty after setup.");
    }

    @Test
    public void testKeyboardSetupMessage() {
        // Capture the printed output during keyboard setup
        midiSetup.keyboardSetup();
        // You could use a custom output stream or a testing library to verify printed outputs
    }
}