package view.model;

import javafx.animation.Transition;
import javafx.scene.Group;
import javafx.util.Duration;


public class Phase3Animation extends Transition {
    private final Group circles;
    private final Group lines;
    private final Group texts;

    public Phase3Animation(Group circles, Group lines, Group texts) {
        this.circles = circles;
        this.lines = lines;
        this.setCycleDuration(Duration.millis(2000));
        this.setCycleCount(-1);
        this.texts = texts;
    }

    @Override
    protected void interpolate(double v) {
        if (v <= 0.1) {
            circles.setOpacity(1);
            lines.setOpacity(1);
            texts.setOpacity(1);
        } else if (v >= 0.49 && v <= 0.51) {
            circles.setOpacity(0);
            lines.setOpacity(0);
            texts.setOpacity(0);
        }
    }
}