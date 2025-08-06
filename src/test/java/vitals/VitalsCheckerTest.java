package vitals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class VitalsCheckerTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    private String captureAndGetOutput(Runnable testCode) {
        System.setOut(new PrintStream(outContent));
        testCode.run();
        return outContent.toString();
    }

    private void assertVitalsFail(float temp, float pulse, float spo2, String... expectedMessages) throws InterruptedException {
        String output = captureAndGetOutput(() -> {
            assertFalse(VitalsChecker.vitalsOk(temp, pulse, spo2));
        });
        for (String message : expectedMessages) {
            assertTrue(output.contains(message));
        }
    }

    private void assertVitalsPass(float temp, float pulse, float spo2) throws InterruptedException {
        assertTrue(VitalsChecker.vitalsOk(temp, pulse, spo2));
    }

    // --- Tests ---

    @Test
    void testAllVitalsValid() throws InterruptedException {
        assertVitalsPass(98, 80, 95);
    }

    @Test
    void testTemperatureBelowMin() throws InterruptedException {
        assertVitalsFail(94, 80, 95, "Temperature is critical!");
    }

    @Test
    void testTemperatureAboveMax() throws InterruptedException {
        assertVitalsFail(103, 80, 95, "Temperature is critical!");
    }

    @Test
    void testPulseBelowMin() throws InterruptedException {
        assertVitalsFail(98, 59, 95, "Pulse Rate is out of range!");
    }

    @Test
    void testPulseAboveMax() throws InterruptedException {
        assertVitalsFail(98, 101, 95, "Pulse Rate is out of range!");
    }

    @Test
    void testSpO2BelowMin() throws InterruptedException {
        assertVitalsFail(98, 80, 89, "Oxygen Saturation out of range!");
    }

    @Test
    void testAllVitalsBelowMin() throws InterruptedException {
        assertVitalsFail(94, 59, 89, 
            "Temperature is critical!", 
            "Pulse Rate is out of range!", 
            "Oxygen Saturation out of range!"
        );
    }

    @Test
    void testAllVitalsAtUpperBoundary() throws InterruptedException {
        assertVitalsPass(102, 100, 90);
    }

    @Test
    void testAllVitalsAtLowerBoundary() throws InterruptedException {
        assertVitalsPass(95, 60, 90);
    }

    @Test
    void testTempBelowPulseAbove() throws InterruptedException {
        assertVitalsFail(94, 101.1f, 95, "Temperature is critical!", "Pulse Rate is out of range!");
    }

    @Test
    void testMultipleFailures() throws InterruptedException {
        assertVitalsFail(102.1f, 59, 89, 
            "Temperature is critical!", 
            "Pulse Rate is out of range!", 
            "Oxygen Saturation out of range!"
        );
    }

    @Test
    void testPulseAboveSpO2Below() throws InterruptedException {
        assertVitalsFail(96, 101, 89, "Pulse Rate is out of range!", "Oxygen Saturation out of range!");
    }

    @Test
    void testTempMaxPulseMin() throws InterruptedException {
        assertVitalsFail(102, 59, 95, "Pulse Rate is out of range!");
    }
}
