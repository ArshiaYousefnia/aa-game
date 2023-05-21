package view.model;

import javafx.animation.Transition;
import javafx.util.Duration;
import view.GameApplicationController;

public class WindSpeedTransition extends Transition {
    private final GameApplicationController controller;
    private final double speed;

    public WindSpeedTransition(GameApplicationController controller, double speed) {
        this.controller = controller;
        this.speed = speed;
        this.setCycleCount(-1);
        this.setCycleDuration(Duration.millis(1000));
    }

    @Override
    protected void interpolate(double v) {
        if (v >= 0.00001) return;

        double degree = controller.getWindDegree() + speed;
        if (degree > 15.0)
            degree = -15.0;

        controller.setWindDegree(((int) (degree * 100)) / 100.0);
    }
}