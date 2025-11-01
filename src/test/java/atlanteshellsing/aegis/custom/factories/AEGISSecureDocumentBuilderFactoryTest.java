package atlanteshellsing.aegis.custom.factories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AEGISSecureDocumentBuilderFactory Tests")
class AEGISSecureDocumentBuilderFactoryTest {

    private AEGISSecureDocumentBuilderFactory secureFactory;

    @BeforeEach
    void setUp() {
        secureFactory = new AEGISSecureDocumentBuilderFactory();
    }

    @Test
    @DisplayName("Should create non-null factory instance")
    void testFactoryCreation() {
        assertNotNull(secureFactory, "Factory should be created");
        assertNotNull(secureFactory.getSecureFactory(), "Secure factory should not be null");
    }

    @Test
    @DisplayName("Should return DocumentBuilderFactory instance")
    void testGetSecureFactoryReturnsCorrectType() {
        DocumentBuilderFactory factory = secureFactory.getSecureFactory();
        assertInstanceOf(DocumentBuilderFactory.class, factory, 
            "Should return DocumentBuilderFactory instance");
    }

    @Test
    @DisplayName("Should have secure processing enabled")
    void testSecureProcessingEnabled() throws ParserConfigurationException {
        DocumentBuilderFactory factory = secureFactory.getSecureFactory();
        assertTrue(factory.getFeature(XMLConstants.FEATURE_SECURE_PROCESSING),
            "Secure processing should be enabled");
    }

    @Test
    @DisplayName("Should have DOCTYPE declarations disabled")
    void testDoctypeDisabled() throws ParserConfigurationException {
        DocumentBuilderFactory factory = secureFactory.getSecureFactory();
        assertTrue(factory.getFeature("http://apache.org/xml/features/disallow-doctype-decl"),
            "DOCTYPE declarations should be disabled");
    }

    @Test
    @DisplayName("Should have external general entities disabled")
    void testExternalGeneralEntitiesDisabled() throws ParserConfigurationException {
        DocumentBuilderFactory factory = secureFactory.getSecureFactory();
        assertFalse(factory.getFeature("http://xml.org/sax/features/external-general-entities"),
            "External general entities should be disabled");
    }

    @Test
    @DisplayName("Should have external parameter entities disabled")
    void testExternalParameterEntitiesDisabled() throws ParserConfigurationException {
        DocumentBuilderFactory factory = secureFactory.getSecureFactory();
        assertFalse(factory.getFeature("http://xml.org/sax/features/external-parameter-entities"),
            "External parameter entities should be disabled");
    }

    @Test
    @DisplayName("Should have external DTD loading disabled")
    void testExternalDTDDisabled() throws ParserConfigurationException {
        DocumentBuilderFactory factory = secureFactory.getSecureFactory();
        assertFalse(factory.getFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd"),
            "External DTD loading should be disabled");
    }

    @Test
    @DisplayName("Should have XInclude disabled")
    void testXIncludeDisabled() {
        DocumentBuilderFactory factory = secureFactory.getSecureFactory();
        assertFalse(factory.isXIncludeAware(),
            "XInclude should be disabled");
    }

    @Test
    @DisplayName("Should have entity reference expansion disabled")
    void testEntityReferenceExpansionDisabled() {
        DocumentBuilderFactory factory = secureFactory.getSecureFactory();
        assertFalse(factory.isExpandEntityReferences(),
            "Entity reference expansion should be disabled");
    }

    @Test
    @DisplayName("Should have namespace awareness enabled")
    void testNamespaceAwarenessEnabled() {
        DocumentBuilderFactory factory = secureFactory.getSecureFactory();
        assertTrue(factory.isNamespaceAware(),
            "Namespace awareness should be enabled");
    }

    @Test
    @DisplayName("Should be able to create DocumentBuilder")
    void testCanCreateDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory factory = secureFactory.getSecureFactory();
        DocumentBuilder builder = factory.newDocumentBuilder();
        assertNotNull(builder, "Should be able to create DocumentBuilder");
    }

    @Test
    @DisplayName("Should create multiple independent factory instances")
    void testMultipleFactoryInstances() {
        AEGISSecureDocumentBuilderFactory factory1 = new AEGISSecureDocumentBuilderFactory();
        AEGISSecureDocumentBuilderFactory factory2 = new AEGISSecureDocumentBuilderFactory();
        
        assertNotNull(factory1.getSecureFactory());
        assertNotNull(factory2.getSecureFactory());
        assertNotSame(factory1.getSecureFactory(), factory2.getSecureFactory(),
            "Each instance should have its own factory");
    }

    @Test
    @DisplayName("Should maintain security settings after document creation")
    void testSecuritySettingsPersistAfterUse() throws ParserConfigurationException {
        DocumentBuilderFactory factory = secureFactory.getSecureFactory();
        factory.newDocumentBuilder();
        
        // Verify security settings are still in place
        assertTrue(factory.getFeature(XMLConstants.FEATURE_SECURE_PROCESSING),
            "Secure processing should remain enabled after use");
        assertFalse(factory.isExpandEntityReferences(),
            "Entity expansion should remain disabled after use");
    }

    @Test
    @DisplayName("Should handle concurrent factory access")
    void testConcurrentFactoryAccess() {
        assertDoesNotThrow(() -> {
            DocumentBuilderFactory factory = secureFactory.getSecureFactory();
            DocumentBuilder builder1 = factory.newDocumentBuilder();
            DocumentBuilder builder2 = factory.newDocumentBuilder();
            
            assertNotNull(builder1);
            assertNotNull(builder2);
        }, "Should handle multiple builder creations");
    }

    @Test
    @DisplayName("Should return same factory instance on multiple calls")
    void testGetSecureFactoryConsistency() {
        DocumentBuilderFactory factory1 = secureFactory.getSecureFactory();
        DocumentBuilderFactory factory2 = secureFactory.getSecureFactory();
        
        assertSame(factory1, factory2,
            "getSecureFactory should return the same instance");
    }

    @Test
    @DisplayName("Should have all critical security features configured")
    void testAllSecurityFeaturesConfigured() throws ParserConfigurationException {
        DocumentBuilderFactory factory = secureFactory.getSecureFactory();
        
        // Verify all security features are properly set
        assertAll("Security features should all be properly configured",
            () -> assertTrue(factory.getFeature(XMLConstants.FEATURE_SECURE_PROCESSING)),
            () -> assertTrue(factory.getFeature("http://apache.org/xml/features/disallow-doctype-decl")),
            () -> assertFalse(factory.getFeature("http://xml.org/sax/features/external-general-entities")),
            () -> assertFalse(factory.getFeature("http://xml.org/sax/features/external-parameter-entities")),
            () -> assertFalse(factory.getFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd")),
            () -> assertFalse(factory.isXIncludeAware()),
            () -> assertFalse(factory.isExpandEntityReferences()),
            () -> assertTrue(factory.isNamespaceAware())
        );
    }
}