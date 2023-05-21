package view.model;

import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import view.GameApplicationController;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import view.LoginMenu;

public class FreezeModeTransition extends Transition {
    private final GameApplicationController controller;
    private final double initialOmega;
    private final ImagePattern imagePattern1 = new ImagePattern(
            new Image(LoginMenu.class.getResource("/assets/freezing/1.png").toExternalForm()));
    private final ImagePattern imagePattern2 = new ImagePattern(
            new Image(LoginMenu.class.getResource("/assets/freezing/2.png").toExternalForm()));
    private final ImagePattern imagePattern3 = new ImagePattern(
            new Image(LoginMenu.class.getResource("/assets/freezing/3.png").toExternalForm()));
    private final ImagePattern imagePattern4 = new ImagePattern(
            new Image(LoginMenu.class.getResource("/assets/freezing/4.png").toExternalForm()));
    private final Circle centralCircle;
    private final Media media = new Media(LoginMenu.class.getResource("/assets/freezing/sound.mp3").toExternalForm());
    private final MediaPlayer mediaPlayer = new MediaPlayer(media);

    public FreezeModeTransition(GameApplicationController controller) {
        this.controller = controller;
        this.initialOmega = controller.getOmega();
        this.setCycleDuration(Duration.millis(controller.getFreefreezeLengthSeconds() * 1000));
        this.setCycleCount(1);
        this.centralCircle = controller.getCentralCircle();
    }


        @Override
    protected void interpolate(double v) {
            if (v <= 0.1) {
                centralCircle.setFill(imagePattern1);
                mediaPlayer.play();
                controller.setOmega(initialOmega * 7);
            }
            else if (v <= 0.4)
                centralCircle.setFill(imagePattern2);
            else if (v <= 0.8) {
                centralCircle.setFill(imagePattern3);
            } else if (v <= 0.95) {
                centralCircle.setFill(imagePattern4);
            } else {
                centralCircle.setFill(Color.BLACK);
                mediaPlayer.stop();
                controller.setOmega(initialOmega);
            }
        }
}

