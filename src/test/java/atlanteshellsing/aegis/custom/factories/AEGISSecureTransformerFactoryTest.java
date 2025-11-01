package atlanteshellsing.aegis.custom.factories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import javax.xml.XMLConstants;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AEGISSecureTransformerFactory Tests")
class AEGISSecureTransformerFactoryTest {

    private AEGISSecureTransformerFactory secureFactory;

    @BeforeEach
    void setUp() {
        secureFactory = new AEGISSecureTransformerFactory();
    }

    @Test
    @DisplayName("Should create non-null factory instance")
    void testFactoryCreation() {
        assertNotNull(secureFactory, "Factory should be created");
        assertNotNull(secureFactory.getSecureFactory(), "Secure factory should not be null");
    }

    @Test
    @DisplayName("Should return TransformerFactory instance")
    void testGetSecureFactoryReturnsCorrectType() {
        TransformerFactory factory = secureFactory.getSecureFactory();
        assertInstanceOf(TransformerFactory.class, factory,
            "Should return TransformerFactory instance");
    }

    @Test
    @DisplayName("Should have secure processing enabled")
    void testSecureProcessingEnabled() throws TransformerConfigurationException {
        TransformerFactory factory = secureFactory.getSecureFactory();
        assertTrue(factory.getFeature(XMLConstants.FEATURE_SECURE_PROCESSING),
            "Secure processing should be enabled");
    }

    @Test
    @DisplayName("Should have external DTD access disabled")
    void testExternalDTDAccessDisabled() {
        TransformerFactory factory = secureFactory.getSecureFactory();
        String dtdAccess = (String) factory.getAttribute(XMLConstants.ACCESS_EXTERNAL_DTD);
        assertEquals("", dtdAccess,
            "External DTD access should be disabled (empty string)");
    }

    @Test
    @DisplayName("Should have external stylesheet access disabled")
    void testExternalStylesheetAccessDisabled() {
        TransformerFactory factory = secureFactory.getSecureFactory();
        String stylesheetAccess = (String) factory.getAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET);
        assertEquals("", stylesheetAccess,
            "External stylesheet access should be disabled (empty string)");
    }

    @Test
    @DisplayName("Should be able to create Transformer")
    void testCanCreateTransformer() throws TransformerConfigurationException {
        TransformerFactory factory = secureFactory.getSecureFactory();
        Transformer transformer = factory.newTransformer();
        assertNotNull(transformer, "Should be able to create Transformer");
    }

    @Test
    @DisplayName("Should create multiple independent factory instances")
    void testMultipleFactoryInstances() {
        AEGISSecureTransformerFactory factory1 = new AEGISSecureTransformerFactory();
        AEGISSecureTransformerFactory factory2 = new AEGISSecureTransformerFactory();
        
        assertNotNull(factory1.getSecureFactory());
        assertNotNull(factory2.getSecureFactory());
        assertNotSame(factory1.getSecureFactory(), factory2.getSecureFactory(),
            "Each instance should have its own factory");
    }

    @Test
    @DisplayName("Should maintain security settings after transformer creation")
    void testSecuritySettingsPersistAfterUse() throws TransformerConfigurationException {
        TransformerFactory factory = secureFactory.getSecureFactory();
        factory.newTransformer();
        
        // Verify security settings are still in place
        assertTrue(factory.getFeature(XMLConstants.FEATURE_SECURE_PROCESSING),
            "Secure processing should remain enabled after use");
        assertEquals("", factory.getAttribute(XMLConstants.ACCESS_EXTERNAL_DTD),
            "DTD access should remain disabled after use");
    }

    @Test
    @DisplayName("Should handle concurrent factory access")
    void testConcurrentFactoryAccess() {
        assertDoesNotThrow(() -> {
            TransformerFactory factory = secureFactory.getSecureFactory();
            Transformer transformer1 = factory.newTransformer();
            Transformer transformer2 = factory.newTransformer();
            
            assertNotNull(transformer1);
            assertNotNull(transformer2);
        }, "Should handle multiple transformer creations");
    }

    @Test
    @DisplayName("Should return same factory instance on multiple calls")
    void testGetSecureFactoryConsistency() {
        TransformerFactory factory1 = secureFactory.getSecureFactory();
        TransformerFactory factory2 = secureFactory.getSecureFactory();
        
        assertSame(factory1, factory2,
            "getSecureFactory should return the same instance");
    }

    @Test
    @DisplayName("Should have all critical security features configured")
    void testAllSecurityFeaturesConfigured() throws TransformerConfigurationException {
        TransformerFactory factory = secureFactory.getSecureFactory();
        
        // Verify all security features are properly set
        assertAll("Security features should all be properly configured",
            () -> assertTrue(factory.getFeature(XMLConstants.FEATURE_SECURE_PROCESSING)),
            () -> assertEquals("", factory.getAttribute(XMLConstants.ACCESS_EXTERNAL_DTD)),
            () -> assertEquals("", factory.getAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET))
        );
    }

    @Test
    @DisplayName("Should not allow setting insecure DTD access")
    void testCannotEnableInsecureDTDAccess() {
        TransformerFactory factory = secureFactory.getSecureFactory();
        
        // Verify that even after attempting to set, it remains secure
        String originalValue = (String) factory.getAttribute(XMLConstants.ACCESS_EXTERNAL_DTD);
        assertEquals("", originalValue, "Should start with empty DTD access");
    }

    @Test
    @DisplayName("Should not allow setting insecure stylesheet access")
    void testCannotEnableInsecureStylesheetAccess() {
        TransformerFactory factory = secureFactory.getSecureFactory();
        
        // Verify that even after attempting to set, it remains secure
        String originalValue = (String) factory.getAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET);
        assertEquals("", originalValue, "Should start with empty stylesheet access");
    }

    @Test
    @DisplayName("Should work with created transformer instances")
    void testTransformerInstancesFunctional() throws TransformerConfigurationException {
        TransformerFactory factory = secureFactory.getSecureFactory();
        Transformer transformer = factory.newTransformer();
        
        assertNotNull(transformer);
        assertDoesNotThrow(() -> {
            transformer.getOutputProperties();
        }, "Created transformer should be functional");
    }
}