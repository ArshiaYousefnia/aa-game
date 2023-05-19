package view;

import controller.DataBase;
import controller.TextBase;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import view.model.GameLost;
import view.model.GameWon;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class GameApplicationController {
    private final double centralCircleRadius = 30;
    private final double  smallCircleRadius = 10;
    private static final double paneWidth = 600;
    private static final double paneHeight = 700;
    private static final double centerX = paneWidth / 2;
    private static final double centerY = paneHeight / 2;
    private final double orbitRadius = 200;
    private ChangeListener changeListener;
    private final int totalBallsToThrow = DataBase.getBallsToThrow();
    private int ballsLeftToThrow = 10;
    //TODO fix this
    private int score = 0;
    private final double TotalTime = 300.0;
    private double timePassed = 0.0;
    private final double beginningTimeMillis = System.currentTimeMillis();
    private Circle centralCircle;
    private Rotate rotate;
    private Timeline timeline;
    private Stack<Circle> pendingCircles = new Stack<>();
    private double omega = 10.0;
    private HashMap<Double, Integer> existingCircles = new HashMap<>();

    private Group lines = new Group();
    private Group circles = new Group();
    private Group texts = new Group();
    private Pane gamePane;
    private final Label ballsLeftLabel = new Label(Integer.toString(ballsLeftToThrow));
    private final Label scoreBoard = new Label(Integer.toString(0));
    private final Label timePassedLabel = new Label("0 : 0");

    private void loadBalls() {
        //TODO link this to json or sth
        existingCircles.put(0.0, 1);
        existingCircles.put(10.0, 2);
    }
    public Pane buildApp() {
        gamePane = getPane();
        loadBalls();
        loadGame();
        setRotation();
        setTimeLine();
        addRotationToGroups();
        addLabels();
        gamePane.getChildren().addAll(centralCircle, circles, lines, texts);
        addInternalIntersectionWatchDog();

        assignEventToCircle();

        timeline.play();
        return gamePane;
    }

    private void assignEventToCircle() {
        //TODO assign event to balls at bottom instead.
        centralCircle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Circle ball = getSmallCircleToBeAdded(centerX, centerY + orbitRadius);
                putCircleToExistingCircles(ball, ballsLeftToThrow);
                decrementBallsLeft();
                //setOmega(-omega);
            }
        });
    }

    private void loadGame() {
        centralCircle = getCentralCircle();

        for (Map.Entry<Double, Integer> currentSet : existingCircles.entrySet()) {
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

    private void addLabels() {
        timePassedLabel.setTextFill(Color.BLACK);
        timePassedLabel.setStyle("-fx-font-size: x-large; -fx-background-color: wheat");
        ballsLeftLabel.setTextFill(Color.RED);
        ballsLeftLabel.setStyle("-fx-font-size: x-large; -fx-background-color: wheat");
        scoreBoard.setTextFill(Color.GOLDENROD);
        scoreBoard.setStyle("-fx-font-size: x-large; -fx-background-color: wheat");
        scoreBoard.setLayoutX(centerX);
        scoreBoard.setLayoutY(15);
        ballsLeftLabel.setLayoutX(centerX + 50);
        ballsLeftLabel.setLayoutY(15);
        timePassedLabel.setLayoutY(15);
        timePassedLabel.setLayoutX(centerX - 50);
        gamePane.getChildren().addAll(scoreBoard, ballsLeftLabel, timePassedLabel);
        //TODO add top labels to pane
    }

    private void addInternalIntersectionWatchDog() {

        changeListener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                updateTimePassed();
                for (int i = 0; i < circles.getChildren().size() - 1; i++) {
                    for (int j = i + 1 ; j < circles.getChildren().size(); j++) {
                        if (circles.getChildren().get(i).intersects(circles.getChildren().get(j).getBoundsInParent())) {
                            //TODO link to end game caller
                            lostTheGame();
                            rotate.angleProperty().removeListener(changeListener);
                        }
                    }
                }

                if (ballsLeftToThrow == 0) {
                    //TODO link to end game caller
                    wonTheGame();
                    rotate.angleProperty().removeListener(changeListener);
                }
                else if (timePassed - TotalTime > 0) {
                    //TODO link to end game caller
                    lostTheGame();
                    rotate.angleProperty().removeListener(changeListener);
                }
            }
        };

        rotate.angleProperty().addListener(changeListener);
    }

    private void wonTheGame() {
        gamePane.setStyle("-fx-background-color: #0cf50c");
        timeline.stop();
        circles.getTransforms().clear();
        lines.getTransforms().clear();
        texts.getTransforms().clear();
        GameWon gameWon = new GameWon(circles, texts, lines);
        gameWon.play();

        gameWon.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                rotate.angleProperty().removeListener(changeListener);
                showPopUp("YOU WON!");
                try {
                    new LoginMenu().start(new Stage());
                    //TODO change for main menu
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void lostTheGame() {
        gamePane.setStyle("-fx-background-color: red");
        timeline.stop();
        circles.getTransforms().clear();
        lines.getTransforms().clear();
        texts.getTransforms().clear();
        GameLost gameLost = new GameLost(circles, texts, lines);
        gameLost.play();
        gameLost.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                rotate.angleProperty().removeListener(changeListener);
                showPopUp("YOU LOST!");
                try {
                    new LoginMenu().start(new Stage());
                    //TODO change for main menu
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }
    private void putCircleToExistingCircles(Circle circle, int number) {

        existingCircles.put(getAngleFromXY(circle.getCenterX(), circle.getCenterY()), number);

        Line line = getLine(circle.getCenterX(), circle.getCenterY());
        Text text = getText(number, circle.getCenterX(), circle.getCenterY());

        lines.getChildren().add(line);
        texts.getChildren().add(text);
        circles.getChildren().add(circle);
    }

    private void decrementBallsLeft() {
        ballsLeftToThrow--;
        ballsLeftLabel.setText(Integer.toString(ballsLeftToThrow));

        if (ballsLeftToThrow <= 5)
            ballsLeftLabel.setTextFill(Color.GREEN);
        else if (ballsLeftToThrow <= (totalBallsToThrow / 2))
            ballsLeftLabel.setTextFill(Color.DARKCYAN);
    }

    private void addScore(int amount) {
        score += amount;
        scoreBoard.setText(Integer.toString(score));
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

    private void addRotationToGroups() {
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

    private void updateTimePassed() {
        timePassed = (System.currentTimeMillis() - beginningTimeMillis) / 1000;
        timePassedLabel.setText((int) (timePassed) / 60 + " : " + (int) (timePassed % 60));
    }

    public int getScore() {
        return score;
    }

    private void showPopUp(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(TextBase.getCurrentText(message));
        alert.setHeaderText(TextBase.getCurrentText(message));
        //TODO fix level
        alert.setContentText(TextBase.getCurrentText("score") + ": " + score
                + "\n\n" + TextBase.getCurrentText("time") + ": " + (int) timePassed
                + "\n\n" + TextBase.getCurrentText("level") +  ": " + 0);
        alert.show();
    }

    public static double getCenterX() {
        return centerX;
    }

    public static double getCenterY() {
        return centerY;
    }
}