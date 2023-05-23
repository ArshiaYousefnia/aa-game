package view;

import controller.TextBase;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

public class PauseUtility {
    private final GameApplicationController controller;

    public PauseUtility(GameApplicationController controller) {
        this.controller = controller;
    }

    public HBox getPauseToolbar() {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setStyle("-fx-background-color: wheat; -fx-border-radius: 10 10 10 10;"
                + "-fx-background-radius:  10 10 10 10; -fx-border-color: black;");

        hBox.setMaxWidth(400);
        hBox.setSpacing(20);
        hBox.setMaxHeight(35);
        hBox.setMinHeight(35);
        hBox.setMinWidth(400);
        hBox.getChildren().addAll(getExitButton(), getRestartButton(), getSaveButton(), getTrackToggleButton(), getInformationButton());
        return hBox;
    }


    private Button getExitButton() {
        Button button = new Button(TextBase.getCurrentText("exit"));

        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    controller.exit();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return button;
    }

    private Button getTrackToggleButton() {
        Button button = new Button(TextBase.getCurrentText("track toggle"));

        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                controller.toggleTrack(button);
            }
        });

        return button;
    }

    private Button getSaveButton() {
        Button button = new Button(TextBase.getCurrentText("save"));

        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                controller.saveGame();
            }
        });

        return button;
    }

    private Button getRestartButton() {
        Button button = new Button(TextBase.getCurrentText("restart"));

        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    controller.restart();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return button;
    }

    private Button getInformationButton() {
        Button button = new Button(TextBase.getCurrentText("help"));

        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(TextBase.getCurrentText("game controls"));
                alert.setContentText(TextBase.getCurrentText("throw balls")
                        + " : " + "Space"
                        + "\n\n" + TextBase.getCurrentText("Freeze")
                        + " : " + "Tab" + "\n\n" + TextBase.getCurrentText("pause") + " : " + "p");
                alert.show();
            }
        });

        return button;
    }
}