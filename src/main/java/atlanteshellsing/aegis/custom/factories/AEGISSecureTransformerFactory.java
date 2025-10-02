package atlanteshellsing.aegis.custom.factories;

import atlanteshellsing.aegis.logging.AEGISLogger;

import javax.xml.XMLConstants;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;

public class AEGISSecureTransformerFactory {

    private TransformerFactory secureFactory;

    public AEGISSecureTransformerFactory() {

        try {
            secureFactory = TransformerFactory.newInstance();

            secureFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            // Disallow external stylesheets/entities
            secureFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            secureFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
        } catch (TransformerConfigurationException e) {
            AEGISLogger.log(AEGISLogger.AEGISLogKey.AEGIS_MAIN, AEGISLogger.AEGISLogLevel.SEVERE, "Failed to configure secure XML transformer", e);
        }
    }

    public TransformerFactory getSecureFactory() { return secureFactory; }
}
