module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.bootstrapfx.core;
    requires kotlin.stdlib;

    opens org.example to javafx.fxml;
    exports org.example;
    exports org.example.controller;
    opens org.example.controller to javafx.fxml;
}
