package vitals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterEach;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

// import static org.junit.jupiter.api.Assertions.*;

class VitalsCheckerTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeAll
    static void setUpBeforeAll() throws Exception {
        // Optional: Could replace Thread.sleep with a no-op using reflection if needed
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    private void captureOutput() {
        System.setOut(new PrintStream(outContent));
    }

    private String getOutput() {
        return outContent.toString();
    }

    @Test
    void testAllVitalsValid() throws InterruptedException {
        assertTrue(VitalsChecker.vitalsOk(98, 80, 95));
    }

    @Test
    void testTemperatureBelowMin() throws InterruptedException {
        captureOutput();
        assertFalse(VitalsChecker.vitalsOk(94, 80, 95));
        assertTrue(getOutput().contains("Temperature is critical!"));
    }

    @Test
    void testTemperatureAboveMax() throws InterruptedException {
        captureOutput();
        assertFalse(VitalsChecker.vitalsOk(103, 80, 95));
        assertTrue(getOutput().contains("Temperature is critical!"));
    }

    @Test
    void testPulseBelowMin() throws InterruptedException {
        captureOutput();
        assertFalse(VitalsChecker.vitalsOk(98, 59, 95));
        assertTrue(getOutput().contains("Pulse Rate is out of range!"));
    }

    @Test
    void testPulseAboveMax() throws InterruptedException {
        captureOutput();
        assertFalse(VitalsChecker.vitalsOk(98, 101, 95));
        assertTrue(getOutput().contains("Pulse Rate is out of range!"));
    }

    @Test
    void testSpO2BelowMin() throws InterruptedException {
        captureOutput();
        assertFalse(VitalsChecker.vitalsOk(98, 80, 89));
        assertTrue(getOutput().contains("Oxygen Saturation out of range!"));
    }

    @Test
    void testAllVitalsBelowMin() throws InterruptedException {
        captureOutput();
        assertFalse(VitalsChecker.vitalsOk(94, 59, 89));
        String output = getOutput();
        assertTrue(output.contains("Temperature is critical!"));
        assertTrue(output.contains("Pulse Rate is out of range!"));
        assertTrue(output.contains("Oxygen Saturation out of range!"));
    }

    @Test
    void testAllVitalsAtUpperBoundary() throws InterruptedException {
        assertTrue(VitalsChecker.vitalsOk(102, 100, 90));
    }

    @Test
    void testAllVitalsAtLowerBoundary() throws InterruptedException {
        assertTrue(VitalsChecker.vitalsOk(95, 60, 90));
    }

    @Test
    void testTempBelowPulseAbove() throws InterruptedException {
        captureOutput();
        assertFalse(VitalsChecker.vitalsOk(94, 101.1f, 95));
        String output = getOutput();
        assertTrue(output.contains("Temperature is critical!"));
        assertTrue(output.contains("Pulse Rate is out of range!"));
    }

    @Test
    void testMultipleFailures() throws InterruptedException {
        captureOutput();
        assertFalse(VitalsChecker.vitalsOk(102.1f, 59, 89));
        String output = getOutput();
        assertTrue(output.contains("Temperature is critical!"));
        assertTrue(output.contains("Pulse Rate is out of range!"));
        assertTrue(output.contains("Oxygen Saturation out of range!"));
    }

    @Test
    void testPulseAboveSpO2Below() throws InterruptedException {
        captureOutput();
        assertFalse(VitalsChecker.vitalsOk(96, 101, 89));
        String output = getOutput();
        assertTrue(output.contains("Pulse Rate is out of range!"));
        assertTrue(output.contains("Oxygen Saturation out of range!"));
    }

    @Test
    void testTempMaxPulseMin() throws InterruptedException {
        captureOutput();
        assertFalse(VitalsChecker.vitalsOk(102, 59, 95));
        String output = getOutput();
        assertTrue(output.contains("Pulse Rate is out of range!"));
    }
}

