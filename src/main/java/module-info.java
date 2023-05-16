module APGameAA {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires com.google.common;


    exports view;
    opens view to javafx.fxml;
}