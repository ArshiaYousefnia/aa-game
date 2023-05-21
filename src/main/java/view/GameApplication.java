package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.DataPackage;

public class GameApplication extends Application {
    private final GameApplicationController controller = new GameApplicationController();
    private final DataPackage dataPackage;

    public GameApplication(DataPackage dataPackage) {
        this.dataPackage = dataPackage;
    }

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        Pane pane = controller.buildApp(dataPackage);//TODO provide data package
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
        controller.directFocus();
    }
}