package atlanteshellsing.aegis.custom.factories;

import atlanteshellsing.aegis.logging.AEGISLogger;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class AEGISSecureDocumentBuilderFactory {

    private DocumentBuilderFactory secureFactory;

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

    public DocumentBuilderFactory getSecureFactory() { return secureFactory; }
}
