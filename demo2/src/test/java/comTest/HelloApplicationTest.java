package comTest;

import com.example.demo.*;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

/*
 *   @ Jean Michel Mugabe
 */
public class HelloApplicationTest extends ApplicationTest {

    private HelloApplication app;

    @BeforeClass
    public static void setUpClass() throws Exception {
        // Initialize the JavaFX toolkit before running tests
        Platform.startup(() -> {});
    }

    @AfterClass
    public static void tearDownClass() {
        Platform.exit();
    }

    @Override
    public void start(Stage stage) throws Exception {
        app = new HelloApplication();
        app.start(stage);
    }

    @Test
    public void testStartMethod() {
        // Test if the start method runs without exceptions
        try {
            Stage stage = new Stage();
            app.start(stage);
        } catch (Exception e) {
            assert false : "Application start method should not throw an exception.";
        }
    }
}