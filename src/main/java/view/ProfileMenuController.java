package view;

import controller.DataBase;
import controller.TextBase;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import view.model.CircleButton;

import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.chrono.ChronoLocalDateTime;
import java.util.ArrayList;
import java.util.Random;

public class ProfileMenuController {
    private final static String deleteAccountButtonSource = "/assets/general/delete.png";
    private LeaderBoardsMenuController leaderBoardsMenuController = new LeaderBoardsMenuController();
    private MainMenuController mainMenuController = new MainMenuController();
    private ArrayList<Button> buttons = new ArrayList<>();
    private ArrayList<TextField> textFields = new ArrayList<>();
    private ArrayList<Label> labels = new ArrayList<>();
    private Circle avatarCircle;
    private final static controller.LoginMenuController validator = new controller.LoginMenuController();

    public BorderPane buildMenu() {
        BorderPane borderPane = mainMenuController.getBorderPane();
        VBox vBox = mainMenuController.getVBox();
        vBox.setSpacing(25);
        vBox.setMaxHeight(600);
        mountOnVBox(vBox);
        setEvents();
        borderPane.setCenter(vBox);
        return borderPane;
    }


    private void mountOnVBox(VBox vBox) {
        avatarCircle = leaderBoardsMenuController.getAvatarCircle(DataBase.getCurrentUser(), 70);

        Label label1 = new Label(DataBase.getCurrentUser().getUsername());
        label1.setStyle("-fx-font-size: xx-large");
        Label label2 = new Label();
        labels.add(label1);
        labels.add(label2);

        HBox hBox1 = mainMenuController.getHBox();
        hBox1.setSpacing(30);
        hBox1.setMaxHeight(50);
        hBox1.setMinHeight(50);
        Button toggle = new Button(TextBase.getCurrentText("toggle"));
        Button upload = new Button(TextBase.getCurrentText("upload avatar"));
        buttons.add(toggle);
        buttons.add(upload);

        hBox1.getChildren().addAll(toggle, upload);

        HBox hBox2 = mainMenuController.getHBox();
        hBox2.setSpacing(10);
        hBox2.setMaxHeight(30);
        hBox2.setMinHeight(30);
        Button changeMyUsername = new Button(TextBase.getCurrentText("change my username"));
        buttons.add(changeMyUsername);
        TextField textField = new TextField();
        textFields.add(textField);
        hBox2.getChildren().addAll(changeMyUsername, textField);

        HBox hBox3 = mainMenuController.getHBox();
        hBox3.setSpacing(10);
        hBox3.setMaxHeight(30);
        hBox3.setMinHeight(30);
        Button changeMyPassword = new Button(TextBase.getCurrentText("change my password"));
        buttons.add(changeMyPassword);
        PasswordField passwordField1 = new PasswordField();
        passwordField1.setPromptText(TextBase.getCurrentText("current password"));
        PasswordField passwordField2 = new PasswordField();
        passwordField2.setPromptText(TextBase.getCurrentText("new password"));
        textFields.add(passwordField1);
        textFields.add(passwordField2);
        hBox3.getChildren().addAll(changeMyPassword, passwordField1, passwordField2);

        HBox hBox4 = mainMenuController.getHBox();
        hBox4.setMaxHeight(80);
        hBox4.setMinHeight(80);
        Circle home = CircleButton.getCircleButton(
                MainMenuController.getImagePattern(
                        LeaderBoardsMenuController.getExitButtonSource()), 30, new MainMenu());
        Circle delete = CircleButton.getCircleButton(
                MainMenuController.getImagePattern(deleteAccountButtonSource), 30, new LoginMenu());
        delete.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                DataBase.removeUser(DataBase.getCurrentUser());
                DataBase.setCurrentUser(null);

                try {
                    new LoginMenu().start(LoginMenu.getStage());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        hBox4.getChildren().addAll(home, delete);

        vBox.getChildren().addAll(avatarCircle, label1, label2, hBox1, hBox2, hBox3, hBox4);
    }

    private void clear() {
        for (TextField field : textFields)
            field.clear();
    }

    private void setEvents() {
        buttons.get(0).setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                DataBase.getCurrentUser().toggleDefaultAvatarNumber(DataBase.getDefaultAvatarsCount());
                avatarCircle.setFill(new ImagePattern(DataBase.getCurrentUser().getAvatar()));
            }
        });

        buttons.get(1).setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("png Files", "*.png")
                        ,new FileChooser.ExtensionFilter("jpg Files", "*.jpg"));
                File file = fileChooser.showOpenDialog(LoginMenu.getStage());

                if (file == null) return;
                String noise = Integer.toString(new Random(23123).nextInt(10000)+ 1);
                File file1 = new File("./src/main/resources/assets/avatars/external/", noise + file.getName());
                File file2 = new File("./target/classes/assets/avatars/external/", noise + file.getName());
                try {
                    file1.createNewFile();
                    file2.createNewFile();
                } catch (IOException ignored) {
                }
                try {
                    BufferedImage bufferedImage = ImageIO.read(file);
                    ImageIO.write(bufferedImage, "jpg", file1);
                    ImageIO.write(bufferedImage, "jpg", file2);
                } catch (Exception ignored) {
                }
                try {
                    BufferedImage bufferedImage = ImageIO.read(file);
                    ImageIO.write(bufferedImage, "png", file1);
                    ImageIO.write(bufferedImage, "png", file2);
                } catch (Exception e) {
                }
                DataBase.getCurrentUser().setAvatarPath("/assets/avatars/external/" + noise + file.getName());
                avatarCircle.setFill(new ImagePattern(new Image(file.getPath())));
            }
        });

        buttons.get(2).setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                labels.get(1).setText(validator.changeUsername(textFields.get(0).getText()));
                clear();
                labels.get(0).setText(DataBase.getCurrentUser().getUsername());
            }
        });

        buttons.get(3).setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                labels.get(1).setText(validator.changePassword(textFields.get(1).getText(), textFields.get(2).getText()));
                clear();
            }
        });
    }
}