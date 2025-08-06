package atlanteshellsing.aegis;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AEGISMainApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("Welcome to A.E.G.I.S.");
        Scene scene = new Scene(label, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("A.E.G.I.S. Framework");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}