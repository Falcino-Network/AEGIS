package atlanteshellsing.aegis.custom.factories;

import atlanteshellsing.aegis.logging.AEGISLogger;

import javax.xml.XMLConstants;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;

public class AEGISSecureTransformerFactory {

    private TransformerFactory secureFactory;

    /**
     * Initializes a TransformerFactory preconfigured for secure XML processing.
     *
     * The created factory has XMLConstants.FEATURE_SECURE_PROCESSING enabled and
     * external DTD and external stylesheet access disabled.
     *
     * If configuration fails, a severe log entry is written and {@code secureFactory}
     * may be left uninitialized (null).
     */
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

    /**
 * Provides the TransformerFactory preconfigured for secure XML processing.
 *
 * @return the TransformerFactory with secure processing enabled and external DTD/stylesheets disabled, or `null` if configuration failed
 */
public TransformerFactory getSecureFactory() { return secureFactory; }
}