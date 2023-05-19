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
        this.setCycleDuration(Duration.millis(300));
    }
    @Override
    protected void interpolate(double v) {
        for (int i = 0; i < circles.getChildren().size(); i++) {
            Circle circle = ((Circle) circles.getChildren().get(i));
            circle.setCenterY(2 * circle.getCenterY() - GameApplicationController.getCenterY());
            circle.setCenterX(2 * circle.getCenterX() + GameApplicationController.getCenterX());
            try {
                Line line = ((Line) lines.getChildren().get(i));
                line.setEndY(2 * line.getEndY() - GameApplicationController.getCenterY());
                line.setEndX(2 * line.getEndX() - GameApplicationController.getCenterX());
                Text text = ((Text) texts.getChildren().get(i));
                text.setY(2 * text.getY() - GameApplicationController.getCenterY());
                text.setX(2 * text.getX() - GameApplicationController.getCenterX());
            } catch (Exception ignored) {
            }
        }
    }
}
