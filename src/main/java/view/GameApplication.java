package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import model.DataPackage;

import java.awt.*;

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
        Pane pane = controller.buildApp(dataPackage);
        pane.setBackground(new Background(new BackgroundImage(new Image(LoginMenu.class.getResource("/assets/general/back.jpg").toExternalForm()),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT)));

        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
        controller.directFocus();
    }
}