package atlanteshellsing.aegis.tests;

import atlanteshellsing.aegis.structure.AEGISConfigurationManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class AEGISConfigurationManagerTest {

    static File schemaFile = new File(Objects.requireNonNull(AEGISConfigurationManagerTest.class.getResource("/files/aegis.xsd")).getFile());
    @TempDir
    static File dir;

    @Nested
    @TestMethodOrder(MethodOrderer.MethodName.class)
    class InitConfigDirTest {

        @Test
        void ConfigFileTest_001_TestFilesMade() {
            AEGISConfigurationManager.initUserConfig(Path.of(dir.getPath()));

            assertAll(
                    () -> assertTrue(Files.exists(Path.of(dir.getPath()).resolve("Configuration"))),
                    () -> assertTrue(Files.exists(Path.of(dir.getPath()).resolve("Logs"))),
                    () -> assertTrue(Files.exists(Path.of(dir.getPath()).resolve("Configuration").resolve("configuration.aegis")))
            );
        }

        @Test
        void ConfigFileTest_002_ValidateConfigFile() {
            assertDoesNotThrow(() -> {
                SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                Schema schema = factory.newSchema(new File("src/test/resources/files/aegis.xsd"));

                Validator validator = schema.newValidator();
                validator.validate(new StreamSource(Path.of(dir.getPath()).resolve("Configuration").resolve("configuration.aegis").toFile()));
            });
        }
    }

    @AfterAll
    static void cleanup() {
        schemaFile = null;
        dir = null;
    }
}
