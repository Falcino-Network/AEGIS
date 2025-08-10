package atlanteshellsing.aegis.gui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class AEGISMainGui {

    private final BorderPane pane;
    private final MenuBar menuBar;

    public AEGISMainGui() {
        pane = new BorderPane();
        menuBar = new MenuBar();
        initMenuBar();
    }

    private void initMenuBar() {
        Menu fileMenu = new Menu("File");
        fileMenu.getItems().addAll(
                new MenuItem("New"),
                new MenuItem("Open"),
                new MenuItem("Exit")
        );

        Menu helpMenu = new Menu("Help");
        helpMenu.getItems().add(new MenuItem("About"));

        menuBar.getMenus().addAll(fileMenu, helpMenu);

        initHeader();
    }

    private void initHeader() {
        Label titleLabel = new Label("AEGIS - Alpha");
        titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        titleLabel.setAlignment(Pos.CENTER);

        VBox headerBox = new VBox();
        headerBox.setAlignment(Pos.CENTER);
        headerBox.getChildren().addAll(titleLabel, menuBar);

        pane.setTop(headerBox);
    }

    public Scene createScene(double width, double height) {
        return new Scene(pane, width, height);
    }
}
