package atlanteshellsing.aegis.theme;

import atlanteshellsing.aegis.custom.factories.AEGISSecureDocumentBuilderFactory;
import atlanteshellsing.aegis.custom.factories.AEGISSecureTransformerFactory;
import atlanteshellsing.aegis.logging.AEGISLogger;
import atlanteshellsing.aegis.structure.AEGISConfigurationManager;
import javafx.scene.Scene;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class AEGISThemeManager {

    public static final String DARK_THEME = "/themes/DarkTheme.css";
    public static final String LIGHT_THEME = "/themes/LightTheme.css";

    private static String currentTheme = LIGHT_THEME;

    /**
     * Prevents instantiation of this utility class.
     *
     * @throws IllegalStateException always thrown to indicate the class should not be instantiated
     */
    private AEGISThemeManager() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Apply the specified theme stylesheet to the given Scene and persist the selection.
     *
     * Clears any existing stylesheets on the scene, sets the manager's current theme to the provided
     * theme path (typically AEGISThemeManager.LIGHT_THEME or AEGISThemeManager.DARK_THEME), and adds
     * the corresponding stylesheet resource to the scene.
     *
     * @param scene the JavaFX Scene to update
     * @param theme the classpath resource path to the theme CSS (e.g. AEGISThemeManager.LIGHT_THEME)
     */
    public static void applyTheme(Scene scene, String theme) {
        scene.getStylesheets().clear();
        currentTheme = theme;
        saveTheme();
        scene.getStylesheets().add(Objects.requireNonNull(AEGISThemeManager.class.getResource(theme)).toExternalForm());
    }

    /**
     * Toggle the application's theme between light and dark, apply the new theme to the provided scene, and persist the change.
     *
     * @param scene the JavaFX Scene to which the new theme will be applied
     */
    public static void toggleTheme(Scene scene) {
        if (currentTheme.equals(LIGHT_THEME)) {
            applyTheme(scene, DARK_THEME);
        } else {
            applyTheme(scene, LIGHT_THEME);
        }
    }

    /**
     * Loads the user's theme preference from the application config and applies that theme to the provided scene.
     *
     * If no theme preference exists in the config, the light theme is used. Errors encountered while reading
     * the config are logged and do not propagate.
     *
     * @param scene the JavaFX Scene to which the loaded theme will be applied
     */
    public static void loadTheme(Scene scene) {

        try {
            File configFile = AEGISConfigurationManager.userConfigFile.toFile();

            DocumentBuilderFactory factory = new AEGISSecureDocumentBuilderFactory().getSecureFactory();


            DocumentBuilder builder = factory.newDocumentBuilder();

            Document doc = builder.parse(configFile);

            Element root = doc.getDocumentElement();
            Element preferences = (Element) root.getElementsByTagName("preferences").item(0);
            Element themeElement = preferences != null ? (Element) preferences.getElementsByTagName("theme").item(0) : null;

            if (themeElement == null) {
                currentTheme = LIGHT_THEME;
            } else {
                String storedTheme = themeElement.getTextContent();
                currentTheme = "LightTheme".equals(storedTheme) ? LIGHT_THEME : DARK_THEME;
            }

            applyTheme(scene, currentTheme);


        } catch (ParserConfigurationException | SAXException | IOException e) {
            AEGISLogger.log(AEGISLogger.AEGISLogKey.AEGIS_MAIN, AEGISLogger.AEGISLogLevel.SEVERE, "Something Went Wrong When Loading Theme", e);
            currentTheme = LIGHT_THEME;
            scene.getStylesheets().setAll(
                    Objects.requireNonNull(AEGISThemeManager.class.getResource(LIGHT_THEME)).toExternalForm()
            );
        }
    }

    /**
     * Persists the currently selected theme into the user's configuration XML file.
     *
     * Ensures a <preferences> element and a nested <theme> element exist in the config,
     * sets the <theme> text to either "LightTheme" or "DarkTheme" based on the current theme,
     * and writes the updated document back to the config file using secure XML transformers.
     *
     * Any parse, I/O, or transform failures are caught and logged as severe via AEGISLogger.
     */
    private static void saveTheme() {

        try {

            File configFile = AEGISConfigurationManager.userConfigFile.toFile();

            DocumentBuilderFactory factory = new AEGISSecureDocumentBuilderFactory().getSecureFactory();
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setNamespaceAware(true);

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(configFile);

            Element root = doc.getDocumentElement();
            Element preferences = (Element) root.getElementsByTagName("preferences").item(0);
            if (preferences == null) {
                preferences = doc.createElement("preferences");
                root.appendChild(preferences);
            }
            Element themeElement = (Element) preferences.getElementsByTagName("theme").item(0);
            if (themeElement == null) {
                themeElement = doc.createElement("theme");
                preferences.appendChild(themeElement);
            }

            String theme = getCurrentTheme().equals(LIGHT_THEME) ? "LightTheme" : "DarkTheme";
            themeElement.setTextContent(theme);

            TransformerFactory transformerFactory = new AEGISSecureTransformerFactory().getSecureFactory();
            transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "no");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(configFile);
            transformer.transform(source, result);

        } catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
            AEGISLogger.log(AEGISLogger.AEGISLogKey.AEGIS_MAIN, AEGISLogger.AEGISLogLevel.SEVERE, "Something Went Wrong When Saving Theme", e);
        }
    }

    /**
     * Retrieve the stylesheet path for the currently active theme.
     *
     * @return the current theme stylesheet path; one of {@link #LIGHT_THEME} or {@link #DARK_THEME}
     */
    public static String getCurrentTheme() {
        return currentTheme;
    }
}