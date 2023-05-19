package view;

import controller.DataBase;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class GameApplicationController {
    private final double centralCircleRadius = 30;
    private final double  smallCircleRadius = 10;
    private final double paneWidth = 600;
    private final double paneHeight = 700;
    private final double centerX = paneWidth / 2;
    private final double centerY = paneHeight / 2;
    private final double orbitRadius = 200;
    private final int totalBallsToThrow = DataBase.getBallsToThrow();

    private Circle centralCircle;
    private Rotate rotate;
    private Timeline timeline;
    private Stack<Circle> pendingCircles = new Stack<>();
    private double omega = 30.0;
    private HashMap<Double, Integer> existingcircles = new HashMap<>();

    private Group lines = new Group();
    private Group circles = new Group();
    private Group texts = new Group();

    private void loadBalls() {
        //TODO add this
        existingcircles.put(0.0, 1);
        existingcircles.put(10.0, 2);
    }
    public Pane buildApp() {
        Pane pane = getPane();
        loadBalls();
        loadGame();
        setRotation();
        setTimeLine();
        addRotationTogroups();
        pane.getChildren().addAll(centralCircle, circles, lines, texts);
        addInternalIntersectionWatchDog();

        assignEventToCircle();

        timeline.play();
        return pane;
    }

    private void assignEventToCircle() {
        centralCircle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Circle ball = getSmallCircleToBeAdded(centerX, centerY + orbitRadius);
                putCircleToExistingCircles(ball, 56);
                setOmega(-omega);
            }
        });
    }

    private void loadGame() {
        centralCircle = getCentralCircle();

        for (Map.Entry<Double, Integer> currentSet : existingcircles.entrySet()) {
            double x = getXFromAngle(currentSet.getKey());
            double y = getYFromAngle(currentSet.getKey());
            Circle ball = getSmallCircleForSetup(x, y);

            Line line = getLine(x, y);
            Text text = getText(currentSet.getValue(), x, y);

            circles.getChildren().add(ball);
            lines.getChildren().add(line);
            texts.getChildren().add(text);
        }
    }

    private void addInternalIntersectionWatchDog() {
        rotate.angleProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {

                for (int i = 0; i < circles.getChildren().size() - 1; i++) {
                    for (int j = i + 1 ; j < circles.getChildren().size(); j++) {
                        if (circles.getChildren().get(i).intersects(circles.getChildren().get(j).getBoundsInParent())) {
                            //TODO link to end game caller
                            System.out.println("internal intersection!");
                        }
                    }
                }
            }
        });
    }



    private void putCircleToExistingCircles(Circle circle, int number) {

        existingcircles.put(getAngleFromXY(circle.getCenterX(), circle.getCenterY()), number);

        Line line = getLine(circle.getCenterX(), circle.getCenterY());
        Text text = getText(number, circle.getCenterX(), circle.getCenterY());

        lines.getChildren().add(line);
        texts.getChildren().add(text);
        circles.getChildren().add(circle);
    }

    private Pane getPane() {
        Pane pane = new Pane();
        pane.setMinWidth(paneWidth);
        pane.setMaxWidth(paneWidth);
        pane.setMinHeight(paneHeight);
        pane.setMaxHeight(paneHeight);

        return pane;
    }

    private void setRotation() {
        rotate = new Rotate(0, centerX, centerY);
    }

    private void addRotationTogroups() {
        circles.getTransforms().add(rotate);
        lines.getTransforms().add(rotate);
        texts.getTransforms().add(rotate);
    }

    private void setTimeLine() {
        double angle = rotate.getAngle();
        timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(rotate.angleProperty(), angle)),
                new KeyFrame(Duration.seconds(Math.abs(omega)),
                        new KeyValue(rotate.angleProperty(), angle + (omega < 0 ? -1 : 1) * 360)));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    private void setOmega(double newOmega) {
        timeline.play();
        this.omega = newOmega;
        setTimeLine();
        timeline.play();
    }


    ///////////////////////////////////////////////////////////////////////////////////////
    private Circle getCentralCircle() {
        Circle centralCircle = new Circle(centralCircleRadius);

        centralCircle.setStyle("-fx-background-color: black");
        centralCircle.setCenterX(centerX);
        centralCircle.setCenterY(centerY);

        return centralCircle;
    }

    private Circle getSmallCircleForSetup(double x, double y) {
        Circle circle = new Circle(smallCircleRadius);
        circle.setStyle("-fx-background-color: black");
        circle.setCenterX(x);
        circle.setCenterY(y);
        return circle;
    }

    private Circle getSmallCircleToBeAdded(double x, double y) {
        double desiredDegree = getAngleFromXY(x, y);
        double newX = getXFromAngle(desiredDegree - rotate.getAngle());
        double newY = getYFromAngle(desiredDegree - rotate.getAngle());

        return getSmallCircleForSetup(newX, newY);
    }

    private Line getLine(double endX, double endY) {
        Line line = new Line();

        line.setStartX(centerX);
        line.setStartY(centerY);
        line.setEndX(endX);
        line.setEndY(endY);

        return line;
    }

    private Text getText(int number, double startX, double startY) {
        Text text = new Text(Integer.toString(number));
        text.setFill(Color.WHITE);
        text.setStyle("-fx-font-size: medium");

        text.setX(startX - text.getBoundsInLocal().getCenterX() + text.getBoundsInLocal().getMinX());
        text.setY(startY + smallCircleRadius / 2);
        text.setTextAlignment(TextAlignment.LEFT);
        return text;
    }

    private Double getXFromAngle(Double angle) {
        return centerX - (orbitRadius * Math.sin(Math.toRadians(angle)));
    }

    private Double getYFromAngle(Double angle) {
        return centerY + (orbitRadius * Math.cos(Math.toRadians(angle)));
    }

    private Double getAngleFromXY(Double x, Double y) {
        return Math.toDegrees(Math.atan((centerX - x) / (y - centerY)));
    }

}