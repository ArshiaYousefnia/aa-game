package view.model;

import controller.DataBase;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import view.LoginMenu;

public class CircleButton extends Circle {

    private CircleButton(ImagePattern imagePattern, double radius) {
        super();
        super.setRadius(radius);
        super.setFill(imagePattern);
    }

    public static Circle getCircleIcon(ImagePattern imagePattern, int radius) {
        Circle circle = new CircleButton(imagePattern, radius);
        circle.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                circle.setRadius(circle.getRadius() * 1.05);
            }
        });

        circle.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                circle.setRadius(circle.getRadius() / 1.05);
            }
        });

        return circle;
    }

    public static Circle getCircleButton(ImagePattern imagePattern, int radius, Application applicationToBeCalled) {
        Circle circle = getCircleIcon(imagePattern, radius);
        circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    if (DataBase.isSoundStatus()) {
                        MediaPlayer mediaPlayer = new MediaPlayer(
                                new Media(LoginMenu.class.getResource("/assets/general/click.mp3").toExternalForm()));
                        mediaPlayer.play();
                    }
                    applicationToBeCalled.start(LoginMenu.getStage());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return circle;
    }
}
