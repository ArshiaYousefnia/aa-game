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
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import view.model.CircleButton;

import java.util.ArrayList;

public class SettingsMenuController {
    private ArrayList<Label> labelsToBeUpdated;
    private final String vClosePath = "/assets/settingsMenu/vClose.png";
    private final MainMenuController mainMenuController = new MainMenuController();
    private final String vOpenPath = "/assets/settingsMenu/vOpen.png";


    public BorderPane buildMenu() {
        BorderPane borderPane = mainMenuController.getBorderPane();
        borderPane.setLeft(getLeftSideHBox());


        return borderPane;
    }

    private HBox getRightSideHBox() {
        HBox hBox = getLeftSideHBox();
        hBox.getChildren().clear();


    }

    private HBox getDifficultyHBox() {
        HBox hBox = new HBox();
        hBox.setSpacing(5);

        hBox.getChildren().addAll(getDifficultyLabel(), getDifficultyButton());
        return hBox;
    }

    private HBox getBallCountHBox() {
        HBox hBox = new HBox();
        hBox.setSpacing(5);

        hBox.getChildren().addAll(getBallsLabel(), getBallsButton())
    }

    private Label getBallsLabel() {
        Label balls = new Label(TextBase.getCurrentText("balls count"));


    }
    private Label getDifficultyLabel() {
        Label label = new Label(TextBase.getCurrentText("difficulty"));
        labelsToBeUpdated.add(label);
        return label;
    }

    private HBox getLeftSideHBox() {
        HBox hBox = new HBox();
        hBox.setSpacing(50);
        hBox.setMaxWidth(90);
        hBox.setMinWidth(90);
        hBox.setAlignment(Pos.CENTER);

        hBox.getChildren().addAll(getExitButton(), getLanguageButton(), getSoundStatusCircleButton());
        return hBox;
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
