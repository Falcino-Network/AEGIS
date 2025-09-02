package atlanteshellsing.aegis;

import atlanteshellsing.aegis.gui.AEGISMainGui;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class AEGISMainApplication extends Application {
    
    protected AEGISMainGui mainGUI;
    protected Image logo = new Image(Objects.requireNonNull(getClass().getResource("/images/AEGIS.png")).toExternalForm());

    @Override
    public void start(Stage primaryStage) {
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