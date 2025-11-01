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

    private AEGISThemeManager() {
        throw new IllegalStateException("Utility class");
    }

    public static void applyTheme(Scene scene, String theme) {
        scene.getStylesheets().clear();
        currentTheme = theme;
        saveTheme();
        scene.getStylesheets().add(Objects.requireNonNull(AEGISThemeManager.class.getResource(theme)).toExternalForm());
    }

    public static void toggleTheme(Scene scene) {
        if (currentTheme.equals(LIGHT_THEME)) {
            applyTheme(scene, DARK_THEME);
        } else {
            applyTheme(scene, LIGHT_THEME);
        }
    }

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
        }
    }

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

    public static String getCurrentTheme() {
        return currentTheme;
    }
}
