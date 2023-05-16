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
                    //TODO run main menu
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
    }

    public static Image getLogoImage() {
        return logoImage;
    }

    public void clearOutput(MouseEvent mouseEvent) {
        outputLabel.setText("");
    }
}
