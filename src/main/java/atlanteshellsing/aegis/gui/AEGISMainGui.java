package atlanteshellsing.aegis.gui;

import atlanteshellsing.aegis.components.gui.AEGISTabPane;
import atlanteshellsing.aegis.theme.AEGISThemeManager;
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
    private final AEGISTabPane tabPane;

    public AEGISMainGui() {
        pane = new BorderPane();
        menuBar = new MenuBar();
        tabPane = new AEGISTabPane();

        initMenuBar();

        pane.setCenter(tabPane);

        tabPane.addTab("home", "home", new Label("Welcome to Aegis"));
    }

    private void initMenuBar() {
        Menu fileMenu = new Menu("File");
        fileMenu.getItems().addAll(
                new MenuItem("New"),
                new MenuItem("Open"),
                new MenuItem("Exit")
        );

        Menu viewMenu = new Menu("View");
        MenuItem toggleTheme = new MenuItem("Toggle Theme");
        toggleTheme.setOnAction(action -> AEGISThemeManager.toggleTheme(pane.getScene()));
        viewMenu.getItems().add(toggleTheme);

        Menu helpMenu = new Menu("Help");
        helpMenu.getItems().add(new MenuItem("About"));

        menuBar.getMenus().addAll(fileMenu, helpMenu, viewMenu);

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
        Scene scene = new Scene(pane, width, height);
        AEGISThemeManager.loadTheme(scene);
        return scene;
    }

    public AEGISTabPane getMainTabPane() { return tabPane; }
}
