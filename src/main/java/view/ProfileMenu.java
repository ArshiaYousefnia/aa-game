package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ProfileMenu extends Application {
    private final ProfileMenuController profileMenuController = new ProfileMenuController();

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane borderPane = profileMenuController.buildMenu();
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.show();
    }
}
