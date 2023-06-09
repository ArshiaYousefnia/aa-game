package view.model;

import javafx.animation.Transition;
import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.util.Duration;
import view.GameApplicationController;

public class GameWon extends Transition {
    private final Group circles;
    private final Group texts;
    private final Group lines;

    public GameWon(Group circles, Group texts, Group lines) {
        this.texts = texts;
        this.lines = lines;
        this.circles = circles;
        this.setCycleCount(1);
        this.setCycleDuration(Duration.millis(3000));
    }
    @Override
    protected void interpolate(double v) {
        for (int i = 0; i < circles.getChildren().size(); i++) {
            Circle circle = ((Circle) circles.getChildren().get(i));
            circle.setCenterY(circle.getCenterY() + 0.05 * (circle.getCenterY() - GameApplicationController.getCenterY()));
            circle.setCenterX(circle.getCenterX() + 0.05 * (circle.getCenterX() - GameApplicationController.getCenterX()));
            try {
                Line line = ((Line) lines.getChildren().get(i));
                line.setEndY(line.getEndY() + 0.05 * (line.getEndY() - GameApplicationController.getCenterY()));
                line.setEndX(line.getEndX() + 0.05 * (line.getEndX() - GameApplicationController.getCenterX()));

                Text text = ((Text) texts.getChildren().get(i));
                text.setY(text.getY() + 0.05 * (text.getY() - GameApplicationController.getCenterY()));
                text.setX(text.getX() + 0.05 * (text.getX() - GameApplicationController.getCenterX()));
            } catch (Exception ignored) {
            }
        }
    }
}
