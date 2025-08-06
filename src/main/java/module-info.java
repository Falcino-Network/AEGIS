module atlanteshellsing.aegis {
    requires javafx.controls;
    requires javafx.fxml;

    opens atlanteshellsing.aegis to javafx.fxml;  // Allows FXML reflection
    exports atlanteshellsing.aegis;               // Expose public API
}