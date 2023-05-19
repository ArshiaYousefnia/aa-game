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
            circle.setLayoutY(2 * circle.getLayoutY() - GameApplicationController.getCenterY());
            circle.setLayoutX(2 * circle.getLayoutX() + GameApplicationController.getCenterX());
            try {
                Line line = ((Line) lines.getChildren().get(i));
                line.setEndY(2 * line.getEndY() - GameApplicationController.getCenterY());
                line.setEndX(2 * line.getEndX() - GameApplicationController.getCenterX());

                Text text = ((Text) texts.getChildren().get(i));
                text.setLayoutY(2 * text.getLayoutY() - GameApplicationController.getCenterY());
                text.setLayoutX(2 * text.getLayoutX() - GameApplicationController.getCenterX());
            } catch (Exception ignored) {
            }
        }
    }
}
