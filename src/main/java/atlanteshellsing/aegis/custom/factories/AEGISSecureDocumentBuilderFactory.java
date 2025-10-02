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

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);
            factory.setNamespaceAware(true);

        } catch (ParserConfigurationException e) {
            AEGISLogger.log(AEGISLogger.AEGISLogKey.AEGIS_MAIN, AEGISLogger.AEGISLogLevel.SEVERE, "Failed to configure secure XML parser", e);
        }
    }

    public DocumentBuilderFactory getSecureFactory() { return secureFactory; }
}
