package atlanteshellsing.aegis.logging;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AEGISLogger Tests")
class AEGISLoggerTest {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        System.setErr(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    @DisplayName("Should log FINE level messages with AEGIS_MAIN key")
    void testLogFineWithMainKey() {
        AEGISLogger.log(AEGISLogger.AEGISLogKey.AEGIS_MAIN, 
            AEGISLogger.AEGISLogLevel.FINE, 
            "Test fine message");
        
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("AEGIS_MAIN"), "Output should contain AEGIS_MAIN key");
        assertTrue(output.contains("Test fine message"), "Output should contain the message");
        assertTrue(output.contains("FINE"), "Output should contain FINE level");
    }

    @Test
    @DisplayName("Should log INFO level messages with AEGIS_TOOL key")
    void testLogInfoWithToolKey() {
        AEGISLogger.log(AEGISLogger.AEGISLogKey.AEGIS_TOOL, 
            AEGISLogger.AEGISLogLevel.INFO, 
            "Test info message");
        
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("AEGIS_TOOL"), "Output should contain AEGIS_TOOL key");
        assertTrue(output.contains("Test info message"), "Output should contain the message");
        assertTrue(output.contains("INFO"), "Output should contain INFO level");
    }

    @Test
    @DisplayName("Should log WARNING level messages")
    void testLogWarning() {
        AEGISLogger.log(AEGISLogger.AEGISLogKey.AEGIS_MAIN, 
            AEGISLogger.AEGISLogLevel.WARNING, 
            "Test warning message");
        
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("WARNING"), "Output should contain WARNING level");
        assertTrue(output.contains("Test warning message"), "Output should contain the message");
    }

    @Test
    @DisplayName("Should log SEVERE level messages")
    void testLogSevere() {
        AEGISLogger.log(AEGISLogger.AEGISLogKey.AEGIS_MAIN, 
            AEGISLogger.AEGISLogLevel.SEVERE, 
            "Test severe message");
        
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("SEVERE"), "Output should contain SEVERE level");
        assertTrue(output.contains("Test severe message"), "Output should contain the message");
    }

    @Test
    @DisplayName("Should log exceptions with stack trace")
    void testLogWithException() {
        Exception testException = new RuntimeException("Test exception");
        
        AEGISLogger.log(AEGISLogger.AEGISLogKey.AEGIS_MAIN, 
            AEGISLogger.AEGISLogLevel.SEVERE, 
            "Error occurred", 
            testException);
        
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Error occurred"), "Output should contain the message");
        assertTrue(output.contains("Caused by:"), "Output should contain exception header");
        assertTrue(output.contains("RuntimeException"), "Output should contain exception type");
        assertTrue(output.contains("Test exception"), "Output should contain exception message");
    }

    @Test
    @DisplayName("Should format log messages with timestamp")
    void testLogMessageContainsTimestamp() {
        AEGISLogger.log(AEGISLogger.AEGISLogKey.AEGIS_MAIN, 
            AEGISLogger.AEGISLogLevel.INFO, 
            "Test timestamp");
        
        String output = outputStreamCaptor.toString();
        // Check for timestamp pattern (YYYY-MM-DD HH:MM:SS)
        assertTrue(output.matches(".*\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}.*"),
            "Output should contain formatted timestamp");
    }

    @Test
    @DisplayName("Should handle null messages gracefully")
    void testLogWithNullMessage() {
        assertDoesNotThrow(() -> 
            AEGISLogger.log(AEGISLogger.AEGISLogKey.AEGIS_MAIN, 
                AEGISLogger.AEGISLogLevel.INFO, 
                null),
            "Should handle null message without throwing exception");
    }

    @Test
    @DisplayName("Should handle empty messages")
    void testLogWithEmptyMessage() {
        AEGISLogger.log(AEGISLogger.AEGISLogKey.AEGIS_MAIN, 
            AEGISLogger.AEGISLogLevel.INFO, 
            "");
        
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("AEGIS_MAIN"), "Should still log with empty message");
    }

    @Test
    @DisplayName("Should apply correct color codes for FINE level")
    void testFineColorCode() {
        AEGISLogger.log(AEGISLogger.AEGISLogKey.AEGIS_MAIN, 
            AEGISLogger.AEGISLogLevel.FINE, 
            "Test color");
        
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("\u001B[36m"), "Should contain cyan color code for FINE");
    }

    @Test
    @DisplayName("Should apply correct color codes for INFO level")
    void testInfoColorCode() {
        AEGISLogger.log(AEGISLogger.AEGISLogKey.AEGIS_MAIN, 
            AEGISLogger.AEGISLogLevel.INFO, 
            "Test color");
        
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("\u001B[32m"), "Should contain green color code for INFO");
    }

    @Test
    @DisplayName("Should apply correct color codes for WARNING level")
    void testWarningColorCode() {
        AEGISLogger.log(AEGISLogger.AEGISLogKey.AEGIS_MAIN, 
            AEGISLogger.AEGISLogLevel.WARNING, 
            "Test color");
        
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("\u001B[33m"), "Should contain yellow color code for WARNING");
    }

    @Test
    @DisplayName("Should apply correct color codes for SEVERE level")
    void testSevereColorCode() {
        AEGISLogger.log(AEGISLogger.AEGISLogKey.AEGIS_MAIN, 
            AEGISLogger.AEGISLogLevel.SEVERE, 
            "Test color");
        
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("\u001B[31m"), "Should contain red color code for SEVERE");
    }

    @Test
    @DisplayName("Should reset color codes after logging")
    void testColorReset() {
        AEGISLogger.log(AEGISLogger.AEGISLogKey.AEGIS_MAIN, 
            AEGISLogger.AEGISLogLevel.INFO, 
            "Test reset");
        
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("\u001B[0m"), "Should contain reset color code");
    }

    @Test
    @DisplayName("Should handle nested exceptions")
    void testNestedExceptions() {
        Exception cause = new IllegalArgumentException("Root cause");
        Exception wrapper = new RuntimeException("Wrapper exception", cause);
        
        AEGISLogger.log(AEGISLogger.AEGISLogKey.AEGIS_MAIN, 
            AEGISLogger.AEGISLogLevel.SEVERE, 
            "Nested error", 
            wrapper);
        
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("RuntimeException"), "Should contain wrapper exception");
        assertTrue(output.contains("IllegalArgumentException"), "Should contain root cause");
        assertTrue(output.contains("Root cause"), "Should contain root cause message");
    }

    @Test
    @DisplayName("Should log multiple messages sequentially")
    void testMultipleLogMessages() {
        AEGISLogger.log(AEGISLogger.AEGISLogKey.AEGIS_MAIN, 
            AEGISLogger.AEGISLogLevel.INFO, 
            "First message");
        AEGISLogger.log(AEGISLogger.AEGISLogKey.AEGIS_TOOL, 
            AEGISLogger.AEGISLogLevel.WARNING, 
            "Second message");
        
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("First message"), "Should contain first message");
        assertTrue(output.contains("Second message"), "Should contain second message");
        assertTrue(output.contains("AEGIS_MAIN"), "Should contain MAIN key");
        assertTrue(output.contains("AEGIS_TOOL"), "Should contain TOOL key");
    }

    @Test
    @DisplayName("Should handle very long messages")
    void testLongMessage() {
        String longMessage = "A".repeat(1000);
        
        assertDoesNotThrow(() -> 
            AEGISLogger.log(AEGISLogger.AEGISLogKey.AEGIS_MAIN, 
                AEGISLogger.AEGISLogLevel.INFO, 
                longMessage),
            "Should handle long messages");
        
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains(longMessage), "Should contain the full long message");
    }

    @Test
    @DisplayName("Should handle special characters in messages")
    void testSpecialCharactersInMessage() {
        String specialMessage = "Test: <>&\"'%$#@!";
        
        AEGISLogger.log(AEGISLogger.AEGISLogKey.AEGIS_MAIN, 
            AEGISLogger.AEGISLogLevel.INFO, 
            specialMessage);
        
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains(specialMessage), "Should handle special characters");
    }

    @Test
    @DisplayName("LogColor enum should have correct color codes")
    void testLogColorEnumValues() {
        assertEquals("\u001B[36m", AEGISLogger.LogColor.CYAN.code);
        assertEquals("\u001B[32m", AEGISLogger.LogColor.GREEN.code);
        assertEquals("\u001B[33m", AEGISLogger.LogColor.YELLOW.code);
        assertEquals("\u001B[31m", AEGISLogger.LogColor.RED.code);
        assertEquals("\u001B[0m", AEGISLogger.LogColor.RESET.code);
    }

    @Test
    @DisplayName("AEGISLogKey enum should have expected values")
    void testLogKeyEnumValues() {
        AEGISLogger.AEGISLogKey[] keys = AEGISLogger.AEGISLogKey.values();
        assertEquals(2, keys.length, "Should have 2 log keys");
        assertNotNull(AEGISLogger.AEGISLogKey.valueOf("AEGIS_MAIN"));
        assertNotNull(AEGISLogger.AEGISLogKey.valueOf("AEGIS_TOOL"));
    }

    @Test
    @DisplayName("AEGISLogLevel enum should have expected values")
    void testLogLevelEnumValues() {
        AEGISLogger.AEGISLogLevel[] levels = AEGISLogger.AEGISLogLevel.values();
        assertEquals(4, levels.length, "Should have 4 log levels");
        assertNotNull(AEGISLogger.AEGISLogLevel.valueOf("FINE"));
        assertNotNull(AEGISLogger.AEGISLogLevel.valueOf("INFO"));
        assertNotNull(AEGISLogger.AEGISLogLevel.valueOf("WARNING"));
        assertNotNull(AEGISLogger.AEGISLogLevel.valueOf("SEVERE"));
    }

    @Test
    @DisplayName("AEGISLogLevel should map to correct java.util.logging.Level")
    void testLogLevelMapping() {
        assertEquals(Level.FINE, AEGISLogger.AEGISLogLevel.FINE.level);
        assertEquals(Level.INFO, AEGISLogger.AEGISLogLevel.INFO.level);
        assertEquals(Level.WARNING, AEGISLogger.AEGISLogLevel.WARNING.level);
        assertEquals(Level.SEVERE, AEGISLogger.AEGISLogLevel.SEVERE.level);
    }

    @Test
    @DisplayName("AEGISLogLevel should have correct colors assigned")
    void testLogLevelColorAssignments() {
        assertEquals(AEGISLogger.LogColor.CYAN, AEGISLogger.AEGISLogLevel.FINE.color);
        assertEquals(AEGISLogger.LogColor.GREEN, AEGISLogger.AEGISLogLevel.INFO.color);
        assertEquals(AEGISLogger.LogColor.YELLOW, AEGISLogger.AEGISLogLevel.WARNING.color);
        assertEquals(AEGISLogger.LogColor.RED, AEGISLogger.AEGISLogLevel.SEVERE.color);
    }
}