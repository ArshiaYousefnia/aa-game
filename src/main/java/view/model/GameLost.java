package view.model;

import javafx.animation.Transition;
import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.util.Duration;
import view.GameApplicationController;

public class GameLost extends Transition {
    private final Group circles;
    private final Group texts;
    private final Group lines;
    private final double angle;
    private double v1, v2;
    public GameLost(Group circles, Group texts, Group lines, double angle) {
        this.texts = texts;
        this.lines = lines;
        this.circles = circles;
        this.setCycleCount(1);
        this.setCycleDuration(Duration.millis(3000));
        this.angle = angle;
        v1 = 40 * Math.cos(Math.toRadians(angle));
        v2 = 40 * Math.sin(Math.toRadians(angle));
        this.setDelay(Duration.millis(1000));
    }

    @Override
    protected void interpolate(double v) {
        if (v >= 0.2) {
            v1 *= 1.1;
            v2 *= 1.1;
        }
        for (int i = 0; i < circles.getChildren().size(); i++) {
            Circle circle = ((Circle) circles.getChildren().get(i));
            circle.setCenterY(circle.getCenterY() + v1);
            circle.setCenterX(circle.getCenterX() + v2);
            try {
                Line line = ((Line) lines.getChildren().get(i));
                line.setEndY(line.getEndY() + v1);
                line.setEndX(line.getEndX() + v2);
                Text text = ((Text) texts.getChildren().get(i));
                text.setY(text.getY() + v1);
                text.setX(text.getX() + v2);
            } catch (Exception ignored) {
            }
        }
    }
}
