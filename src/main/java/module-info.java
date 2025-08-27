module atlanteshellsing.aegis {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.compiler;

    opens atlanteshellsing.aegis to javafx.fxml;  // Allows FXML reflection
    exports atlanteshellsing.aegis;               // Expose public API
    exports  atlanteshellsing.aegis.gui;
    exports atlanteshellsing.aegis.components.gui;
}