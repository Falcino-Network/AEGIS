package atlanteshellsing.aegis;

import atlanteshellsing.aegis.gui.AEGISMainGui;
import atlanteshellsing.aegis.structure.AEGISConfigurationManager;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.nio.file.Files;
import java.util.Objects;

public class AEGISMainApplication extends Application {
    
    protected AEGISMainGui mainGUI;
    protected Image logo = new Image(Objects.requireNonNull(getClass().getResource("/images/AEGIS.png")).toExternalForm());

    /**
     * Initializes the application UI: ensures the user configuration directory exists, creates the main GUI,
     * sets the scene and application icon on the provided primary stage, and displays the stage.
     *
     * @param primaryStage the primary JavaFX Stage to initialize and show
     */
    @Override
    public void start(Stage primaryStage) {

        if(!Files.exists(AEGISConfigurationManager.userAppDataDir)) {
            AEGISConfigurationManager.initUserConfig();
        }

        mainGUI = new AEGISMainGui();

        primaryStage.setScene(mainGUI.createScene(1280, 800));
        primaryStage.getIcons().clear();
        primaryStage.getIcons().add(logo);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}