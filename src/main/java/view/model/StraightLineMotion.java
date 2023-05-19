package view.model;

import javafx.animation.Transition;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import view.GameApplicationController;

public class StraightLineMotion extends Transition {
    private final Circle circle;
    private final double upSpeed = 7;

    public StraightLineMotion(Circle circle) {
        this.circle = circle;
        this.setCycleDuration(Duration.millis(1000));
        this.setCycleCount(-1);
    }

    @Override
    protected void interpolate(double v) {
        circle.setCenterY(circle.getCenterY() - upSpeed);
        circle.setCenterX(circle.getCenterX() + GameApplicationController.getWindSpeed() / 10);
    }
}
