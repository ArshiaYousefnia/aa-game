package view;

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
    private Button LogIn;
    @FXML
    private Button signUp;
    @FXML
    private Button guest;
    @FXML
    private ImageView logoImageView;
    private static String logoPictureSource = "/assets/loginMenu/gameLogo.png";
    private static Image logoImage = new Image(LoginMenuController.class.getResource(logoPictureSource).toString());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logoImageView.setImage(logoImage);

        LogIn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

            }
        });
    }

    public static Image getLogoImage() {
        return logoImage;
    }
}
