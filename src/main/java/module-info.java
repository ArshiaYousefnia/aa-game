module APGameAA {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires com.google.gson;
    requires com.google.common;
    requires java.desktop;


    exports view;
    exports model;
    opens model to com.google.gson;
    opens view.model to com.google.gson;
    opens view to com.google.gson, javafx.fxml;
}