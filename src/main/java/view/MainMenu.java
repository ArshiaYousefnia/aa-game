package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainMenu extends Application {
    private final MainMenuController controller = new MainMenuController();

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane borderPane = controller.buildMenu();
        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.show();
    }
}
