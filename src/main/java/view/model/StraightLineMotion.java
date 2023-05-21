package view.model;

import javafx.animation.Transition;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import view.GameApplicationController;

public class StraightLineMotion extends Transition {
    private final Circle circle;
    private final double upSpeed = 12;
    private final double degree;

    public StraightLineMotion(Circle circle, double degree) {
        this.circle = circle;
        this.setCycleDuration(Duration.millis(1000));
        this.setCycleCount(-1);
        this.degree = degree;
    }

    @Override
    protected void interpolate(double v) {
        circle.setCenterY(circle.getCenterY() - upSpeed);
        circle.setCenterX(circle.getCenterX() + upSpeed * Math.tan(Math.toRadians(degree)));
    }
}
