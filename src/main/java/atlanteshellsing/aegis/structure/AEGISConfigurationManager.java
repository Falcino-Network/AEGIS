package atlanteshellsing.aegis.structure;

import atlanteshellsing.aegis.custom.factories.AEGISSecureDocumentBuilderFactory;
import atlanteshellsing.aegis.custom.factories.AEGISSecureTransformerFactory;
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
    public static final Path configurationDir = userAppDataDir.resolve("Configuration");
    public static final Path userConfigFile = configurationDir.resolve("configuration.aegis");

    /**
 * Prevents instantiation of AEGISConfigurationManager.
 */
private AEGISConfigurationManager() {}

    /**
     * Determines the base user-specific application data directory for Aegis based on the operating system.
     *
     * @return the Path to the Aegis application data directory; on Windows this is {@code %APPDATA%/Aegis}, on other systems this is {@code $HOME/.config/Aegis}
     */
    private static Path getUserAppDataPath() {
        String os = System.getProperty("os.name").toLowerCase();
        if(os.contains("win")) {
            return Paths.get(System.getenv("APPDATA"), "Aegis");
        } else {
            return Paths.get(System.getProperty("user.home"), ".config", "Aegis");
        }
    }

    /**
     * Initializes the user's configuration storage by ensuring the configuration directory
     * and a "Logs" subdirectory exist, and by creating the default configuration file if missing.
     *
     * <p>On I/O failure, a severe error is logged and initialization stops.</p>
     */
    public static void initUserConfig() {
        try {
                Files.createDirectories(configurationDir);
                Files.createDirectories(userAppDataDir.resolve("Logs"));

                createConfigFile();

        } catch (IOException e) {
            AEGISLogger.log(AEGISLogger.AEGISLogKey.AEGIS_MAIN, AEGISLogger.AEGISLogLevel.SEVERE, "Configuration Files Could Not Be Created", e);
        }
    }

    /**
     * Creates the default user configuration file at the configured path if it does not already exist.
     *
     * The created file is an XML configuration with a root element `aegisConfig` (attribute `schemaVersion="1"`)
     * and a `preferences` child containing a `theme` element set to `LightTheme`.
     * If the configuration file already exists, the method returns without modifying it.
     */
    private static void createConfigFile() {
        try {

            if(Files.exists(userConfigFile)) {
                return;
            }

            DocumentBuilderFactory factory = new AEGISSecureDocumentBuilderFactory().getSecureFactory();
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

            TransformerFactory transformerFactory = new AEGISSecureTransformerFactory().getSecureFactory();
            transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(AEGISConfigurationManager.userConfigFile.toFile());

            transformer.transform(source, result);

        } catch (ParserConfigurationException | TransformerException e) {
            AEGISLogger.log(AEGISLogger.AEGISLogKey.AEGIS_MAIN, AEGISLogger.AEGISLogLevel.SEVERE, "Could not make Configuration File", e);
        }


    }
}