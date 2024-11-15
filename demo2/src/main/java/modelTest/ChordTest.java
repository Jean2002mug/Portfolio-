package modelTest;

/**
 * @author Jean Michel Mugabe
*/
import model.Chord;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class ChordTest {

    private Chord chord;

    @Before
    public void setUp() {
        // Create an instance of Chord for testing
        chord = new Chord(60, 1000, 0, 1);  // Middle C, with duration of 1000 ms, complexity between 0 and 1
    }

    @Test
    public void testChordInitialization() {
        // Verify that the chord is initialized correctly
        assertEquals("Root note should be 60 (Middle C)", 60, chord.getRootNote());
        assertTrue("Notes set should not be empty", !chord.getNotes().isEmpty());
        assertEquals("Duration should be 1000", 1000, chord.getDuration());
    }

    @Test
    public void testGetNotes() {
        // Check if the generated notes are within valid MIDI range (0-127)
        Set<Integer> notes = chord.getNotes();
        for (Integer note : notes) {
            assertTrue("Note should be between 0 and 127", note >= 0 && note <= 127);
        }
    }

    @Test
    public void testChordSize() {
        // Verify the size of the chord
        assertTrue("Chord size should be greater than or equal to 1", chord.size() >= 1);
    }

    @Test
    public void testNoteName() {
        // Verify that the note name is generated correctly
        String noteName = chord.noteName();
        assertNotNull("Note name should not be null", noteName);
        assertTrue("Note name should contain 'C'", noteName.contains("C"));
    }

    @Test
    public void testToString() {
        // Check that the chord's toString method returns a non-empty string representation
        String chordString = chord.toString();
        assertNotNull("Chord string representation should not be null", chordString);
        assertTrue("Chord string representation should not be empty", !chordString.isEmpty());
        assertTrue("Chord string representation should contain note names", chordString.contains("C"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidRootNoteLow() {
        // Test initialization with an invalid low root note
        new Chord(-1, 1000, 0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidRootNoteHigh() {
        // Test initialization with an invalid high root note
        new Chord(128, 1000, 0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidDuration() {
        // Test initialization with an invalid duration
        new Chord(60, -100, 0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidComplexityRangeLow() {
        // Test initialization with minComplexity less than allowed value
        new Chord(60, 1000, -1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidComplexityRangeHigh() {
        // Test initialization with maxComplexity greater than allowed value
        new Chord(60, 1000, 0, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidComplexityMinGreaterThanMax() {
        // Test initialization where minComplexity is greater than maxComplexity
        new Chord(60, 1000, 1, 0);
    }
}