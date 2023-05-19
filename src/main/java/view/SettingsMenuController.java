package view;

import controller.DataBase;
import controller.TextBase;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import view.model.CircleButton;

import java.util.ArrayList;

public class SettingsMenuController {
    private ArrayList<Label> labelsToBeUpdated = new ArrayList<>();
    private final String vClosePath = "/assets/settingsMenu/vClose.png";
    private final MainMenuController mainMenuController = new MainMenuController();
    private final String vOpenPath = "/assets/settingsMenu/vOpen.png";


    public BorderPane buildMenu() {
        BorderPane borderPane = mainMenuController.getBorderPane();
        borderPane.setLeft(getLeftSideVBox());
        borderPane.setRight(getRightSideVBox());
        return borderPane;
    }

    private VBox getRightSideVBox() {
        VBox vBox = getLeftSideVBox();
        vBox.getChildren().clear();
        vBox.setMinWidth(120);
        vBox.setMaxWidth(120);
        vBox.getChildren().addAll(getDifficultyHBox(), getBallCountHBox());
        return vBox;
    }

    private HBox getDifficultyHBox() {
        HBox hBox = new HBox();
        hBox.setSpacing(20);

        hBox.getChildren().addAll(getDifficultyLabel(), getDifficultyButton());
        return hBox;
    }

    private HBox getBallCountHBox() {
        HBox hBox = new HBox();
        hBox.setSpacing(5);

        hBox.getChildren().addAll(getBallsLabel(), getBallsButton());
        return hBox;
    }

    private Label getBallsLabel() {
        Label balls = new Label(TextBase.getCurrentText("balls count"));
        labelsToBeUpdated.add(balls);
        return balls;
    }
    private Label getDifficultyLabel() {
        Label label = new Label(TextBase.getCurrentText("difficulty"));
        labelsToBeUpdated.add(label);
        return label;
    }

    private VBox getLeftSideVBox() {
        VBox vBox = new VBox();
        vBox.setSpacing(30);
        vBox.setMaxWidth(60);
        vBox.setMinWidth(60);
        vBox.setAlignment(Pos.CENTER_LEFT);

        vBox.getChildren().addAll(getExitButton(), getLanguageButton(), getSoundStatusCircleButton());
        return vBox;
    }



    private Button getBallsButton() {
        Button button = new Button(Integer.toString(DataBase.getBallsToThrow()));

        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                DataBase.incrementBallsToThrow();
                button.setText(Integer.toString(DataBase.getBallsToThrow()));
            }
        });

        return button;
    }

    private Circle getSoundStatusCircleButton() {
        Circle circle = CircleButton.getCircleButton(getCurrentVoiceImagePattern(), 20, null);

        circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                DataBase.toggleSound();
                circle.setFill(getCurrentVoiceImagePattern());
            }
        });

        return circle;
    }

    private Button getLanguageButton() {
        Button button = new Button(DataBase.getLanguage());

        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                DataBase.toggleLanguage();

                button.setText(DataBase.getLanguage());

                toggleLanguages();
            }
        });

        return button;
    }

    private void toggleLanguages() {
        labelsToBeUpdated.get(0).setText(TextBase.getCurrentText("difficulty"));
        labelsToBeUpdated.get(1).setText(TextBase.getCurrentText("balls count"));
    }

    private Button getDifficultyButton() {
        Button button = new Button(Integer.toString(DataBase.getDifficulty()));

        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                DataBase.incrementDifficulty();

                button.setText(Integer.toString(DataBase.getDifficulty()));
            }
        });

        return button;
    }

    private  Circle getExitButton() {
        Circle circle = CircleButton.getCircleButton(
                new ImagePattern(
                        new Image(SettingsMenuController.class.getResource("/assets/general/home.png").toString())), 30, new MainMenu());
        return circle;
    }

    private ImagePattern getCurrentVoiceImagePattern() {
        return new ImagePattern(new Image(SettingsMenuController.class.getResource(DataBase.isSoundStatus() ? vOpenPath : vClosePath).toString()));
    }
}
