package view.model;

import javafx.animation.Transition;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class Phase2Animation extends Transition {
    private final Group group;
    private final double initialRadius;
    private final double coefficient = 1.09;

    public Phase2Animation(Group group, double initialRadius) {
        this.group = group;
        this.initialRadius = initialRadius;
        this.setCycleDuration(Duration.millis(2000));
        this.setCycleCount(-1);
    }

    @Override
    protected void interpolate(double v) {
        if (v <= 0.1)
            setRadius(initialRadius);
        else if (v >= 0.49 && v <= 0.51)
            setRadius(initialRadius * coefficient);
    }


    private void setRadius(double radius) {
        for (Node circle : group.getChildren())
            ((Circle) circle).setRadius(radius);
    }
}