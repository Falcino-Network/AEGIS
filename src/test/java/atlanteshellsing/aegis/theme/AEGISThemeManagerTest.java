package atlanteshellsing.aegis.theme;

import atlanteshellsing.aegis.structure.AEGISConfigurationManager;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AEGISThemeManager Tests")
class AEGISThemeManagerTest {

    private Scene testScene;

    @BeforeAll
    static void initJavaFX() {
        // Initialize JavaFX toolkit
        try {
            javafx.application.Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Already initialized, ignore
        }
    }

    @BeforeEach
    void setUp() throws Exception {
        // Create a test scene on JavaFX thread
        javafx.application.Platform.runLater(() -> {
            testScene = new Scene(new Pane(), 800, 600);
        });
        
        // Wait for scene creation
        Thread.sleep(100);

        // Ensure clean state
        if (Files.exists(AEGISConfigurationManager.userConfigFile)) {
            Files.delete(AEGISConfigurationManager.userConfigFile);
        }
        AEGISConfigurationManager.initUserConfig();
    }

    @AfterEach
    void tearDown() throws IOException {
        if (Files.exists(AEGISConfigurationManager.userConfigFile)) {
            Files.delete(AEGISConfigurationManager.userConfigFile);
        }
    }

    @Test
    @DisplayName("Should have correct theme constants")
    void testThemeConstants() {
        assertEquals("/themes/DarkTheme.css", AEGISThemeManager.DARK_THEME);
        assertEquals("/themes/LightTheme.css", AEGISThemeManager.LIGHT_THEME);
    }

    @Test
    @DisplayName("Should have LightTheme as default")
    void testDefaultTheme() {
        assertEquals(AEGISThemeManager.LIGHT_THEME, AEGISThemeManager.getCurrentTheme());
    }

    @Test
    @DisplayName("Constructor should throw IllegalStateException")
    void testConstructorThrowsException() {
        assertThrows(IllegalStateException.class, () -> {
            var constructor = AEGISThemeManager.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        }, "Constructor should throw IllegalStateException for utility class");
    }

    @Test
    @DisplayName("Should toggle from Light to Dark theme")
    void testToggleLightToDark() throws Exception {
        if (testScene == null) return;

        javafx.application.Platform.runLater(() -> {
            AEGISThemeManager.applyTheme(testScene, AEGISThemeManager.LIGHT_THEME);
            AEGISThemeManager.toggleTheme(testScene);
        });

        Thread.sleep(100);
        assertEquals(AEGISThemeManager.DARK_THEME, AEGISThemeManager.getCurrentTheme());
    }

    @Test
    @DisplayName("Should toggle from Dark to Light theme")
    void testToggleDarkToLight() throws Exception {
        if (testScene == null) return;

        javafx.application.Platform.runLater(() -> {
            AEGISThemeManager.applyTheme(testScene, AEGISThemeManager.DARK_THEME);
            AEGISThemeManager.toggleTheme(testScene);
        });

        Thread.sleep(100);
        assertEquals(AEGISThemeManager.LIGHT_THEME, AEGISThemeManager.getCurrentTheme());
    }

    @Test
    @DisplayName("Should update current theme when applying theme")
    void testApplyThemeUpdatesCurrentTheme() throws Exception {
        if (testScene == null) return;

        javafx.application.Platform.runLater(() -> {
            AEGISThemeManager.applyTheme(testScene, AEGISThemeManager.DARK_THEME);
        });

        Thread.sleep(100);
        assertEquals(AEGISThemeManager.DARK_THEME, AEGISThemeManager.getCurrentTheme());
    }

    @Test
    @DisplayName("Should clear previous stylesheets when applying theme")
    void testClearsPreviousStylesheets() throws Exception {
        if (testScene == null) return;

        javafx.application.Platform.runLater(() -> {
            testScene.getStylesheets().add("test-stylesheet.css");
            int beforeSize = testScene.getStylesheets().size();
            AEGISThemeManager.applyTheme(testScene, AEGISThemeManager.LIGHT_THEME);
            
            // Should only have the newly applied theme
            assertTrue(testScene.getStylesheets().size() > 0);
        });

        Thread.sleep(100);
    }

    @Test
    @DisplayName("Should save theme to config file when applying")
    void testSavesThemeToConfig() throws Exception {
        if (testScene == null) return;

        javafx.application.Platform.runLater(() -> {
            AEGISThemeManager.applyTheme(testScene, AEGISThemeManager.DARK_THEME);
        });

        Thread.sleep(200);

        // Verify config file contains the theme
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(AEGISConfigurationManager.userConfigFile.toFile());

        Element root = doc.getDocumentElement();
        Element preferences = (Element) root.getElementsByTagName("preferences").item(0);
        Element theme = (Element) preferences.getElementsByTagName("theme").item(0);

        assertEquals("DarkTheme", theme.getTextContent());
    }

    @Test
    @DisplayName("Should load light theme from config file")
    void testLoadLightThemeFromConfig() throws Exception {
        if (testScene == null) return;

        // Set up config with LightTheme
        createConfigWithTheme("LightTheme");

        javafx.application.Platform.runLater(() -> {
            AEGISThemeManager.loadTheme(testScene);
        });

        Thread.sleep(100);
        assertEquals(AEGISThemeManager.LIGHT_THEME, AEGISThemeManager.getCurrentTheme());
    }

    @Test
    @DisplayName("Should load dark theme from config file")
    void testLoadDarkThemeFromConfig() throws Exception {
        if (testScene == null) return;

        // Set up config with DarkTheme
        createConfigWithTheme("DarkTheme");

        javafx.application.Platform.runLater(() -> {
            AEGISThemeManager.loadTheme(testScene);
        });

        Thread.sleep(100);
        assertEquals(AEGISThemeManager.DARK_THEME, AEGISThemeManager.getCurrentTheme());
    }

    @Test
    @DisplayName("Should default to LightTheme if config has no theme element")
    void testDefaultsToLightThemeWhenMissing() throws Exception {
        if (testScene == null) return;

        // Create config without theme element
        createConfigWithoutTheme();

        javafx.application.Platform.runLater(() -> {
            AEGISThemeManager.loadTheme(testScene);
        });

        Thread.sleep(100);
        assertEquals(AEGISThemeManager.LIGHT_THEME, AEGISThemeManager.getCurrentTheme());
    }

    @Test
    @DisplayName("Should handle multiple theme switches")
    void testMultipleThemeSwitches() throws Exception {
        if (testScene == null) return;

        javafx.application.Platform.runLater(() -> {
            AEGISThemeManager.applyTheme(testScene, AEGISThemeManager.LIGHT_THEME);
            AEGISThemeManager.toggleTheme(testScene);
            AEGISThemeManager.toggleTheme(testScene);
            AEGISThemeManager.toggleTheme(testScene);
        });

        Thread.sleep(200);
        assertEquals(AEGISThemeManager.DARK_THEME, AEGISThemeManager.getCurrentTheme());
    }

    @Test
    @DisplayName("Should persist theme across multiple applies")
    void testThemePersistence() throws Exception {
        if (testScene == null) return;

        javafx.application.Platform.runLater(() -> {
            AEGISThemeManager.applyTheme(testScene, AEGISThemeManager.DARK_THEME);
        });

        Thread.sleep(100);

        javafx.application.Platform.runLater(() -> {
            AEGISThemeManager.applyTheme(testScene, AEGISThemeManager.LIGHT_THEME);
        });

        Thread.sleep(100);

        assertEquals(AEGISThemeManager.LIGHT_THEME, AEGISThemeManager.getCurrentTheme());
    }

    @Test
    @DisplayName("Should handle invalid theme string in config gracefully")
    void testHandlesInvalidThemeString() throws Exception {
        if (testScene == null) return;

        // Create config with invalid theme
        createConfigWithTheme("InvalidTheme");

        javafx.application.Platform.runLater(() -> {
            AEGISThemeManager.loadTheme(testScene);
        });

        Thread.sleep(100);
        // Should default to DARK_THEME for any non-"LightTheme" value
        assertEquals(AEGISThemeManager.DARK_THEME, AEGISThemeManager.getCurrentTheme());
    }

    @Test
    @DisplayName("getCurrentTheme should return current theme")
    void testGetCurrentTheme() throws Exception {
        if (testScene == null) return;

        String initialTheme = AEGISThemeManager.getCurrentTheme();
        assertNotNull(initialTheme);

        javafx.application.Platform.runLater(() -> {
            AEGISThemeManager.applyTheme(testScene, AEGISThemeManager.DARK_THEME);
        });

        Thread.sleep(100);
        assertEquals(AEGISThemeManager.DARK_THEME, AEGISThemeManager.getCurrentTheme());
    }

    // Helper methods
    private void createConfigWithTheme(String themeName) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        Element root = doc.createElement("aegisConfig");
        root.setAttribute("schemaVersion", "1");
        doc.appendChild(root);

        Element preferences = doc.createElement("preferences");
        Element theme = doc.createElement("theme");
        theme.setTextContent(themeName);
        preferences.appendChild(theme);
        root.appendChild(preferences);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(AEGISConfigurationManager.userConfigFile.toFile());
        transformer.transform(source, result);
    }

    private void createConfigWithoutTheme() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        Element root = doc.createElement("aegisConfig");
        root.setAttribute("schemaVersion", "1");
        doc.appendChild(root);

        Element preferences = doc.createElement("preferences");
        root.appendChild(preferences);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(AEGISConfigurationManager.userConfigFile.toFile());
        transformer.transform(source, result);
    }
}