package view;

import controller.DataBase;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import model.DataPackage;
import model.GameData;
import view.model.CircleButton;

public class MainMenuController {
    private static String leaderBoardsButtonSource = "/assets/mainMenu/leaderboards.png";
    private static String playButtonSource = "/assets/mainMenu/play.png";
    private static String resumeButtonSource = "/assets/mainMenu/resume.png";
    private static String settingsButtonSource = "/assets/mainMenu/settings.png";
    private static String exitButtonSource = "/assets/mainMenu/exit.png";

    protected static ImagePattern getImagePattern(String source) {
        return new ImagePattern(new Image(MainMenuController.class.getResource(source).toString()));
    }

    public BorderPane buildMenu() {
        BorderPane borderPane = getBorderPane();
        VBox vBox = getVBox();
        HBox hBox = getHBox();
        mountCenterButtons(vBox);
        mountBottomButtons(hBox);
        borderPane.setCenter(vBox);
        borderPane.setBottom(hBox);

        return borderPane;
    }

    protected BorderPane getBorderPane() {
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(50));
        borderPane.setMinHeight(700);
        borderPane.setMaxHeight(700);
        borderPane.setMaxWidth(600);
        borderPane.setStyle("-fx-background-color: #eeeddf");
        borderPane.setMinWidth(600);
        return borderPane;
    }

    protected VBox getVBox() {
        VBox vBox = new VBox();
        vBox.setSpacing(100);
        vBox.setAlignment(Pos.valueOf("CENTER"));
        vBox.setStyle("-fx-border-color: blue");
        vBox.setStyle("-fx-border-radius: 10");
        return vBox;
    }

    private void mountCenterButtons(VBox vBox) {
        int radius1 = 80, radius2 = 60;
        HBox hBox1 = getHBox();
        GameApplication gameApplication = new GameApplication( new DataPackage(
                GameData.getNewGameGameData(DataBase.getDifficulty(), DataBase.getCurrentBalls(), DataBase.getBallsToThrow()),
                GameData.getNewGameGameData(DataBase.getDifficulty(), DataBase.getCurrentBalls(), DataBase.getBallsToThrow())));
        hBox1.getChildren().add(CircleButton.getCircleButton(
                getImagePattern(playButtonSource), radius1, gameApplication));
        Application application = new MainMenu();

        if (DataBase.getCurrentUser() != null && DataBase.getCurrentUser().getDataPackage() != null)
            application = new GameApplication(DataBase.getCurrentUser().getDataPackage());
        hBox1.getChildren().add(CircleButton.getCircleButton(
                    getImagePattern(resumeButtonSource), radius1, application));
        HBox hBox2 = getHBox();
        hBox2.getChildren().add(CircleButton.getCircleButton(
                getImagePattern(leaderBoardsButtonSource), radius2, new LeaderBoardsMenu()));
        hBox2.getChildren().add(CircleButton.getCircleButton(
                getImagePattern(settingsButtonSource), radius2, new SettingsMenu()));

        vBox.getChildren().add(hBox1);
        vBox.getChildren().add(hBox2);
    }

    protected HBox getHBox() {
        HBox hBox = new HBox();
        hBox.setSpacing(100);
        hBox.setMaxHeight(150);
        hBox.setMinHeight(150);
        hBox.setAlignment(Pos.valueOf("CENTER"));
        return hBox;
    }

    private void mountBottomButtons(HBox hBox) {
        int radius = 30;
        Application application = new MainMenu();
        if (DataBase.getCurrentUser() != null)
            application = new ProfileMenu();
        Image guest = new Image(
                LoginMenu.class.getResource("/assets/avatars/guest.jpg").toExternalForm());
        hBox.getChildren().add(CircleButton.getCircleButton(
                new ImagePattern((DataBase.getCurrentUser() == null) ? guest : DataBase.getCurrentUser().getAvatar()), 30, application));
        hBox.getChildren().add(CircleButton.getCircleButton(
                getImagePattern(exitButtonSource), 30, new LoginMenu()));

    }
}
