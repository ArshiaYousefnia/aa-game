package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SettingsMenu extends Application {
    private final SettingsMenuController settingsMenuController = new SettingsMenuController();
    @Override
    public void start(Stage stage) throws Exception {
        BorderPane borderPane = settingsMenuController.buildMenu();
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.show();
    }
}
