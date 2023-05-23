package view;

import controller.DataBase;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginMenuController implements Initializable {
    @FXML
    private Label outputLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private TextField username;
    @FXML
    private Label passwordLabel;
    @FXML
    private PasswordField password;
    @FXML
    private Button logIn;
    @FXML
    private Button signUp;
    @FXML
    private Button guest;
    @FXML
    private ImageView logoImageView;
    private final controller.LoginMenuController controller = new controller.LoginMenuController();
    private static String logoPictureSource = "/assets/loginMenu/gameLogo.png";
    private static Image logoImage = new Image(LoginMenuController.class.getResource(logoPictureSource).toString());
    private final Stage stage = LoginMenu.getStage();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logoImageView.setImage(logoImage);

        logIn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String result = controller.logIn(username.getText(), password.getText());
                outputLabel.setText(result);

                username.clear();
                password.clear();

                if (DataBase.getCurrentUser() != null) {
                    MainMenu mainMenu = new MainMenu();
                    try {
                        mainMenu.start(stage);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        signUp.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String result;
                result = controller.signUp(username.getText(), password.getText());

                outputLabel.setText(result);
            }
        });

        guest.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    new MainMenu().start(stage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public static Image getLogoImage() {
        return logoImage;
    }

    public void clearOutput(MouseEvent mouseEvent) {
        outputLabel.setText("");
    }
}
