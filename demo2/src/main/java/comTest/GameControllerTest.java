package comTest;

import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Chord;
import model.MeasureGenerator;
import model.MidiInputProcessor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GameControllerTest {

    @Mock
    private MidiInputProcessor mockInputProcessor;

    @Mock
    private MeasureGenerator mockMeasureGenerator;

    @Mock
    private Stage mockStage;

    @Mock
    private Label mockCountDownLabel;

    @Mock
    private Label mockChordLabel;

    private GameController gameController;

    @Before
    public void setUp() {
        gameController = new GameController();
        gameController.setStage(mockStage);
    }

    @Test
    public void testInitialize() {
        // Arrange
        int minComplexity = 1;
        int maxComplexity = 5;
        int timeRemaining = 120;

        // Mock behavior of MeasureGenerator
        List<Chord> mockMeasure = new ArrayList<>();
        mockMeasure.add(new Chord());  // Mock a simple chord
        when(mockMeasureGenerator.nextMeasure(minComplexity, maxComplexity)).thenReturn(mockMeasure);

        // Act
        gameController.initialize(mockMeasureGenerator, minComplexity, maxComplexity, timeRemaining);

        // Assert
        verify(mockMeasureGenerator, times(1)).nextMeasure(minComplexity, maxComplexity);
        // Verify that the new measure is set in the chord label
        assert gameController.currentMeasure.equals(mockMeasure);
        verify(mockCountDownLabel, times(1)).setText(Mockito.anyString());  // Check if countdown starts
    }

    @Test
    public void testStartCountDown() {
        // Arrange
        int timeRemaining = 5;  // 5 seconds
        gameController.timeRemaining = timeRemaining;

        // Act
        gameController.startCountDown();

        // Simulate countdown ticking
        gameController.countdownTimer.getKeyFrames().get(0).getAction().handle(null);

        // Assert
        assert gameController.timeRemaining == 4;
        verify(mockCountDownLabel, times(1)).setText(Mockito.anyString());
    }

    @Test
    public void testCorrectChord() {
        // Arrange
        Set<Integer> currentNotes = new HashSet<>();
        currentNotes.add(1);
        gameController.currentNotes = currentNotes;

        Chord mockChord = mock(Chord.class);
        Set<Integer> mockChordNotes = new HashSet<>();
        mockChordNotes.add(1);
        when(mockChord.getNotes()).thenReturn(mockChordNotes);
        gameController.currentMeasure = new ArrayList<>();
        gameController.currentMeasure.add(mockChord);
        gameController.currentBeat = 0;

        // Act
        boolean result = gameController.correctChord();

        // Assert
        assert result;
        assert gameController.currentBeat == 1;  // Beat should advance
        assert gameController.score == 1;  // Score should increment
    }

    @Test
    public void testMoveToNextPage() throws Exception {
        // Arrange
        int score = 10;
        gameController.score = score;

        // Act
        gameController.moveToNextPage();

        // Assert
        verify(mockStage, times(1)).setScene(Mockito.any());  // Check that the scene is updated
    }

    @Test
    public void testMeasureComplete() {
        // Arrange
        gameController.currentMeasure = new ArrayList<>();
        gameController.currentMeasure.add(mock(Chord.class));  // Add a mock chord to the measure
        gameController.currentBeat = 1;  // Simulate that the beat has been advanced

        // Act
        boolean result = gameController.measureComplete();

        // Assert
        assert result;  // Since we only have one chord, the measure should be complete
    }
}