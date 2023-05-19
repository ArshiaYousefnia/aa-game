package view.model;

import controller.DataBase;
import controller.TextBase;
import javafx.animation.Transition;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.util.Duration;
import view.GameApplicationController;
import view.LoginMenu;
import view.MainMenu;

public class GameLost extends Transition {
    private final Group circles;
    private final Group texts;
    private final Group lines;

    public GameLost(Group circles, Group texts, Group lines) {
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
            circle.setCenterY(circle.getCenterY() + 10);
            circle.setCenterX((GameApplicationController.getCenterX() + 16 * circle.getCenterX()) / 17  );
            try {
                Line line = ((Line) lines.getChildren().get(i));
                line.setEndY(line.getEndY() + 10);
                line.setEndX((GameApplicationController.getCenterX() + 16 * line.getEndX()) / 17);
                Text text = ((Text) texts.getChildren().get(i));
                text.setY(text.getY() + 10);
                text.setX((GameApplicationController.getCenterX() + 16 * text.getX()) / 17);
            } catch (Exception ignored) {
            }
        }
    }
}
