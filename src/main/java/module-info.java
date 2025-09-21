module atlanteshellsing.aegis {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
	requires java.compiler;
    requires java.xml;

    opens atlanteshellsing.aegis to javafx.fxml;  // Allows FXML reflection
    exports atlanteshellsing.aegis;               // Expose public API
    exports  atlanteshellsing.aegis.gui;
    exports atlanteshellsing.aegis.components.gui;
    exports atlanteshellsing.aegis.theme;
}