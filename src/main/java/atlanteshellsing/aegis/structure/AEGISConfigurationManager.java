package atlanteshellsing.aegis.structure;

import atlanteshellsing.aegis.logging.AEGISLogger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AEGISConfigurationManager {

    public static final Path userAppDataDir = getUserAppDataPath();

    private AEGISConfigurationManager() {}

    private static Path getUserAppDataPath() {
        String os = System.getProperty("os.name").toLowerCase();
        if(os.contains("win")) {
            return Paths.get(System.getenv("APPDATA"), "Aegis");
        } else {
            return Paths.get(System.getProperty("user.home"), ".config", "Aegis");
        }
    }

    public static void initUserConfig(Path configDir) {
        try {
            if(configDir != null) {
                Files.createDirectories(configDir.resolve("Configuration"));
                Files.createDirectories(configDir.resolve("Logs"));

                createConfigFile(configDir.resolve("Configuration"));
            }
        } catch (IOException e) {
            AEGISLogger.log(AEGISLogger.AEGISLogKey.AEGIS_MAIN, AEGISLogger.AEGISLogLevel.SEVERE, "Configuration Files Could Not Be Created", e);
        }
    }

    private static void createConfigFile(Path configDir) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setExpandEntityReferences(false);
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document doc = builder.newDocument();

            Element root = doc.createElement("aegisConfig");
            root.setAttribute("schemaVersion", "1");
            doc.appendChild(root);

            Element pref = doc.createElement("preferences");
            Element theme = doc.createElement("theme");
            theme.appendChild(doc.createTextNode("LightTheme"));
            pref.appendChild(theme);

            root.appendChild(pref);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(configDir.resolve("configuration.aegis").toFile());

            transformer.transform(source, result);

        } catch (ParserConfigurationException | TransformerException e) {
            AEGISLogger.log(AEGISLogger.AEGISLogKey.AEGIS_MAIN, AEGISLogger.AEGISLogLevel.SEVERE, "Could not make Configuration File", e);
        }


    }
}
