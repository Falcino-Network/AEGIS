package atlanteshellsing.aegis.structure;

import atlanteshellsing.aegis.logging.AEGISLogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AEGISConfigurationManager {

    public static final Path userAppDataDir = getUserAppDataPath();

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
            }
        } catch (IOException e) {
            AEGISLogger.log(AEGISLogger.AEGISLogKey.AEGIS_MAIN, AEGISLogger.AEGISLogLevel.SEVERE, "Configuration Files Could Not Be Created", e);
        }
    }
}
