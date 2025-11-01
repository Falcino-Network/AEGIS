package atlanteshellsing.aegis.custom.factories;

import atlanteshellsing.aegis.logging.AEGISLogger;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class AEGISSecureDocumentBuilderFactory {

    private DocumentBuilderFactory secureFactory;

    /**
     * Creates a DocumentBuilderFactory preconfigured for secure XML parsing.
     *
     * Configures the factory to enable secure processing, disallow DOCTYPE declarations, disable external general and parameter entities, prevent loading external DTDs, disable XInclude processing, disable expansion of entity references, and enable namespace awareness. Any ParserConfigurationException raised while applying these settings is logged. 
     */
    public AEGISSecureDocumentBuilderFactory() {

        try {
            secureFactory = DocumentBuilderFactory.newInstance();

            secureFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            secureFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            secureFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            secureFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            secureFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

            secureFactory.setXIncludeAware(false);
            secureFactory.setExpandEntityReferences(false);
            secureFactory.setNamespaceAware(true);

        } catch (ParserConfigurationException e) {
            AEGISLogger.log(AEGISLogger.AEGISLogKey.AEGIS_MAIN, AEGISLogger.AEGISLogLevel.SEVERE, "Failed to configure secure XML parser", e);
        }
    }

    /**
 * Provides the configured DocumentBuilderFactory for secure XML parsing.
 *
 * @return the DocumentBuilderFactory configured with secure parsing features
 */
public DocumentBuilderFactory getSecureFactory() { return secureFactory; }
}