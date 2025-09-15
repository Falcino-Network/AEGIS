package atlanteshellsing.aegis.theme;

import javafx.scene.Scene;

import java.util.Objects;

public class AEGISThemeManager {

    public static final String DARK_THEME = "/themes/DarkTheme.css";
    public static final String LIGHT_THEME = "/themes/LightTheme.css";

    private static String currentTheme = LIGHT_THEME;

    public static void applyTheme(Scene scene, String theme) {
        scene.getStylesheets().clear();
        currentTheme = theme;
        scene.getStylesheets().add(Objects.requireNonNull(AEGISThemeManager.class.getResource(theme)).toExternalForm());
    }

    public static void toggleTheme(Scene scene) {
        if (currentTheme.equals(LIGHT_THEME)) {
            applyTheme(scene, DARK_THEME);
        } else {
            applyTheme(scene, LIGHT_THEME);
        }
    }

    public static String getCurrentTheme() {
        return currentTheme;
    }
}
