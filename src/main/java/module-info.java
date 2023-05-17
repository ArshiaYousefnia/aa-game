module APGameAA {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires com.google.common;


    exports view;
    opens view to javafx.fxml;
    opens model to com.google.gson;
    opens view.model to com.google.gson;
}