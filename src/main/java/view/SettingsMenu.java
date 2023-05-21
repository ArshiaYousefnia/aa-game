package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SettingsMenu extends Application {
    private final SettingsMenuController settingsMenuController = new SettingsMenuController();
    @Override
    public void start(Stage stage) throws Exception {
        Pane pane = settingsMenuController.buildMenu();
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }
}
