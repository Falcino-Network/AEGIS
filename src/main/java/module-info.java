module atlanteshellsing.aegis {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;

    opens atlanteshellsing.aegis to javafx.fxml;  // Allows FXML reflection
    exports atlanteshellsing.aegis;               // Expose public API
}