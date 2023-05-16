package view;

import com.google.gson.Gson;
import controller.DataBase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.User;

import java.io.FileNotFoundException;
import java.net.URL;

public class LoginMenu extends Application {
    private static Stage mainStage;
    private static final String fxmlSource = "/fxml/LoginMenu.fxml";
    private static final URL sourceUrl = LoginMenu.class.getResource(fxmlSource);

    public static void main(String[] args) throws FileNotFoundException {
        DataBase.fetchUsers();
        launch(args);
        DataBase.pushUsers();
    }

    @Override
    public void start(Stage stage) throws Exception {
        mainStage = stage;
        BorderPane borderPane = FXMLLoader.load(sourceUrl);
        Scene loginMenuScene = new Scene(borderPane);
        Image myImage = LoginMenuController.getLogoImage();

        stage.getIcons().add(myImage);
        stage.setTitle("aa");
        mainStage.setScene(loginMenuScene);
        mainStage.show();
    }


    public static Stage getStage() {
        return mainStage;
    }
}
