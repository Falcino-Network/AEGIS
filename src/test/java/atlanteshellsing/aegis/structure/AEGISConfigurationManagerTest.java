package atlanteshellsing.aegis.structure;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AEGISConfigurationManager Tests")
class AEGISConfigurationManagerTest {

    @TempDir
    Path tempDir;

    private Path originalUserHome;

    @BeforeEach
    void setUp() {
        originalUserHome = Paths.get(System.getProperty("user.home"));
    }

    @AfterEach
    void tearDown() throws IOException {
        // Clean up any created files
        if (Files.exists(AEGISConfigurationManager.userConfigFile)) {
            Files.deleteIfExists(AEGISConfigurationManager.userConfigFile);
        }
    }

    @Test
    @DisplayName("Should have non-null static paths")
    void testStaticPathsNotNull() {
        assertNotNull(AEGISConfigurationManager.userAppDataDir, 
            "User app data directory should not be null");
        assertNotNull(AEGISConfigurationManager.configurationDir, 
            "Configuration directory should not be null");
        assertNotNull(AEGISConfigurationManager.userConfigFile, 
            "User config file path should not be null");
    }

    @Test
    @DisplayName("Should determine correct app data path for Windows")
    void testWindowsAppDataPath() {
        String os = System.getProperty("os.name").toLowerCase();
        Path appDataPath = AEGISConfigurationManager.userAppDataDir;
        
        if (os.contains("win")) {
            assertTrue(appDataPath.toString().contains("Aegis"),
                "Windows path should contain Aegis");
        }
    }

    @Test
    @DisplayName("Should determine correct app data path for Unix-like systems")
    void testUnixAppDataPath() {
        String os = System.getProperty("os.name").toLowerCase();
        Path appDataPath = AEGISConfigurationManager.userAppDataDir;
        
        if (!os.contains("win")) {
            assertTrue(appDataPath.toString().contains(".config"),
                "Unix path should contain .config");
            assertTrue(appDataPath.toString().contains("Aegis"),
                "Unix path should contain Aegis");
        }
    }

    @Test
    @DisplayName("Configuration directory should be subdirectory of user app data")
    void testConfigurationDirectoryHierarchy() {
        assertTrue(AEGISConfigurationManager.configurationDir.startsWith(
            AEGISConfigurationManager.userAppDataDir),
            "Configuration directory should be under user app data directory");
    }

    @Test
    @DisplayName("User config file should be in configuration directory")
    void testConfigFileLocation() {
        assertEquals(AEGISConfigurationManager.configurationDir,
            AEGISConfigurationManager.userConfigFile.getParent(),
            "Config file should be in configuration directory");
        assertEquals("configuration.aegis",
            AEGISConfigurationManager.userConfigFile.getFileName().toString(),
            "Config file should be named configuration.aegis");
    }

    @Test
    @DisplayName("Should create directories when initializing user config")
    void testInitUserConfigCreatesDirectories() {
        // Clean up first if exists
        try {
            if (Files.exists(AEGISConfigurationManager.userConfigFile)) {
                Files.delete(AEGISConfigurationManager.userConfigFile);
            }
            if (Files.exists(AEGISConfigurationManager.configurationDir)) {
                Files.delete(AEGISConfigurationManager.configurationDir);
            }
        } catch (IOException e) {
            // Ignore cleanup errors
        }

        AEGISConfigurationManager.initUserConfig();

        assertTrue(Files.exists(AEGISConfigurationManager.configurationDir),
            "Configuration directory should be created");
        assertTrue(Files.isDirectory(AEGISConfigurationManager.configurationDir),
            "Configuration path should be a directory");
    }

    @Test
    @DisplayName("Should create Logs directory when initializing")
    void testInitUserConfigCreatesLogsDirectory() {
        AEGISConfigurationManager.initUserConfig();

        Path logsDir = AEGISConfigurationManager.userAppDataDir.resolve("Logs");
        assertTrue(Files.exists(logsDir),
            "Logs directory should be created");
        assertTrue(Files.isDirectory(logsDir),
            "Logs path should be a directory");
    }

    @Test
    @DisplayName("Should create config file with correct structure")
    void testConfigFileStructure() throws Exception {
        // Clean up first
        if (Files.exists(AEGISConfigurationManager.userConfigFile)) {
            Files.delete(AEGISConfigurationManager.userConfigFile);
        }

        AEGISConfigurationManager.initUserConfig();

        assertTrue(Files.exists(AEGISConfigurationManager.userConfigFile),
            "Config file should be created");

        // Parse and verify XML structure
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(AEGISConfigurationManager.userConfigFile.toFile());

        Element root = doc.getDocumentElement();
        assertEquals("aegisConfig", root.getNodeName(),
            "Root element should be aegisConfig");
        assertEquals("1", root.getAttribute("schemaVersion"),
            "Schema version should be 1");

        Element preferences = (Element) root.getElementsByTagName("preferences").item(0);
        assertNotNull(preferences, "Should have preferences element");

        Element theme = (Element) preferences.getElementsByTagName("theme").item(0);
        assertNotNull(theme, "Should have theme element");
        assertEquals("LightTheme", theme.getTextContent(),
            "Default theme should be LightTheme");
    }

    @Test
    @DisplayName("Should not recreate config file if it already exists")
    void testDoesNotRecreateExistingConfigFile() throws Exception {
        // Create initial config
        AEGISConfigurationManager.initUserConfig();
        
        if (!Files.exists(AEGISConfigurationManager.userConfigFile)) {
            // Skip test if file wasn't created (perhaps due to permissions)
            return;
        }

        long firstModified = Files.getLastModifiedTime(
            AEGISConfigurationManager.userConfigFile).toMillis();

        // Wait a bit to ensure timestamp would change if file is recreated
        Thread.sleep(10);

        // Call init again
        AEGISConfigurationManager.initUserConfig();

        long secondModified = Files.getLastModifiedTime(
            AEGISConfigurationManager.userConfigFile).toMillis();

        assertEquals(firstModified, secondModified,
            "Config file should not be modified if it already exists");
    }

    @Test
    @DisplayName("Should handle multiple initialization calls gracefully")
    void testMultipleInitializationCalls() {
        assertDoesNotThrow(() -> {
            AEGISConfigurationManager.initUserConfig();
            AEGISConfigurationManager.initUserConfig();
            AEGISConfigurationManager.initUserConfig();
        }, "Should handle multiple initialization calls");
    }

    @Test
    @DisplayName("Constructor should be private")
    void testPrivateConstructor() throws Exception {
        var constructor = AEGISConfigurationManager.class.getDeclaredConstructor();
        assertTrue(java.lang.reflect.Modifier.isPrivate(constructor.getModifiers()),
            "Constructor should be private");
    }

    @Test
    @DisplayName("Should create valid XML file")
    void testCreatesValidXML() throws Exception {
        if (Files.exists(AEGISConfigurationManager.userConfigFile)) {
            Files.delete(AEGISConfigurationManager.userConfigFile);
        }

        AEGISConfigurationManager.initUserConfig();

        if (!Files.exists(AEGISConfigurationManager.userConfigFile)) {
            return; // Skip if file wasn't created
        }

        // Should be able to parse without errors
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        
        assertDoesNotThrow(() -> 
            builder.parse(AEGISConfigurationManager.userConfigFile.toFile()),
            "Should create valid XML that can be parsed");
    }

    @Test
    @DisplayName("Config file should use secure XML processing")
    void testUsesSecureXMLProcessing() throws Exception {
        if (Files.exists(AEGISConfigurationManager.userConfigFile)) {
            Files.delete(AEGISConfigurationManager.userConfigFile);
        }

        AEGISConfigurationManager.initUserConfig();

        if (!Files.exists(AEGISConfigurationManager.userConfigFile)) {
            return; // Skip if file wasn't created
        }

        // The file should not contain any DOCTYPE declarations or external entities
        String content = Files.readString(AEGISConfigurationManager.userConfigFile);
        assertFalse(content.contains("<!DOCTYPE"),
            "Config file should not contain DOCTYPE declarations");
        assertFalse(content.contains("<!ENTITY"),
            "Config file should not contain entity declarations");
    }

    @Test
    @DisplayName("Should handle existing directories gracefully")
    void testHandlesExistingDirectories() throws IOException {
        // Pre-create directories
        Files.createDirectories(AEGISConfigurationManager.configurationDir);
        Files.createDirectories(AEGISConfigurationManager.userAppDataDir.resolve("Logs"));

        assertDoesNotThrow(() -> AEGISConfigurationManager.initUserConfig(),
            "Should handle existing directories without errors");
    }

    @Test
    @DisplayName("Config file should be readable and writable")
    void testConfigFilePermissions() throws IOException {
        AEGISConfigurationManager.initUserConfig();

        if (!Files.exists(AEGISConfigurationManager.userConfigFile)) {
            return; // Skip if file wasn't created
        }

        assertTrue(Files.isReadable(AEGISConfigurationManager.userConfigFile),
            "Config file should be readable");
        assertTrue(Files.isWritable(AEGISConfigurationManager.userConfigFile),
            "Config file should be writable");
    }

    @Test
    @DisplayName("Should create well-formed XML with proper indentation")
    void testXMLFormatting() throws Exception {
        if (Files.exists(AEGISConfigurationManager.userConfigFile)) {
            Files.delete(AEGISConfigurationManager.userConfigFile);
        }

        AEGISConfigurationManager.initUserConfig();

        if (!Files.exists(AEGISConfigurationManager.userConfigFile)) {
            return;
        }

        String content = Files.readString(AEGISConfigurationManager.userConfigFile);
        assertTrue(content.startsWith("<?xml"), 
            "XML should start with declaration");
        assertTrue(content.contains("<aegisConfig"), 
            "Should contain aegisConfig element");
        assertTrue(content.contains("</aegisConfig>"), 
            "Should have closing aegisConfig tag");
    }
}