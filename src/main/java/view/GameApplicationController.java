package view;

import controller.TextBase;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
import view.model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GameApplicationController {
    private final double centralCircleRadius = 50;
    private final double  smallCircleRadius = 10;
    private static final double paneWidth = 600;
    private static final double paneHeight = 700;
    private static final double centerX = paneWidth / 2;
    private static final double centerY = paneHeight / 2;
    private final double orbitRadius = 200;
    private final double beginningPointDistanceFromCenter = 270;
    private ChangeListener changeListener;
    private final int totalBallsToThrow = 10;
    private int ballsLeftToThrow = totalBallsToThrow;
    //TODO fix this
    private int score = 0;
    private double windDegree = 0;
    private double windSpeed = 1.5;
    private final double TotalTime = 300.0;
    private double timePassed = 0.0;
    private double lastDirectionReverseTime = 0.0;
    private final double beginningTimeMillis = System.currentTimeMillis();
    private Circle centralCircle;
    private Rotate rotate;
    private Timeline timeline;
    private Circle targetCircle;
    private double reserveCircleX = centerX;
    private Circle reserveCircle;
    private StraightLineMotion straightLineMotion;
    private Phase2Animation phase2Animation;
    private Phase3Animation phase3Animation;
    private WindSpeedTransition windSpeedTransition;
    private Random rand = new Random();
    private double omega = 5.0;
    private HashMap<Double, Integer> existingCircles = new HashMap<>();
    private Group lines = new Group();
    private Group circles = new Group();
    private Group texts = new Group();
    private Pane gamePane;
    private final Label ballsLeftLabel = new Label(Integer.toString(ballsLeftToThrow));
    private final Label scoreBoard = new Label(Integer.toString(0));
    private final Label timePassedLabel = new Label("0 : 0");
    private final Label windDegreeLabel = new Label(Double.toString(windDegree));

    private void loadBalls() {
        //TODO link this to json or sth
        existingCircles.put(0.0, null);
        existingCircles.put(10.0, null);
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
        addMainWatchDog();

        assignEventToPane();

        timeline.play();
        return gamePane;
    }

    private void assignEventToPane() {
        //TODO assign event to keys instead.
        gamePane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (targetCircle != null || ballsLeftToThrow == 0)
                    return;
                targetCircle = reserveCircle;

                straightLineMotion = new StraightLineMotion(targetCircle, windDegree);
                straightLineMotion.play();

                reserveCircle = null;

                if (ballsLeftToThrow > 1) {
                    reserveCircle = getSmallCircleForSetup(reserveCircleX, centerY + beginningPointDistanceFromCenter);
                    gamePane.getChildren().add(reserveCircle);
                }
            }
        });
    }

    private void loadGame() {
        centralCircle = getCentralCircle();
        //TODO check if you have to trigger phase animation after loading again
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
        reserveCircle = getSmallCircleForSetup(centerX, centerY + beginningPointDistanceFromCenter);
        gamePane.getChildren().add(reserveCircle);
    }

    private void addLabels() {
        timePassedLabel.setTextFill(Color.BLACK);
        timePassedLabel.setStyle(
                "-fx-font-size: x-large; -fx-background-color: wheat; -fx-border-radius: 10 10 10 10;" +
                        " -fx-background-radius:  10 10 10 10; -fx-border-color: black;");
        ballsLeftLabel.setTextFill(Color.RED);
        ballsLeftLabel.setStyle(
                "-fx-font-size: x-large; -fx-background-color: wheat; -fx-border-radius: 10 10 10 10;" +
                        " -fx-background-radius:  10 10 10 10; -fx-border-color: black;");
        scoreBoard.setTextFill(Color.BROWN);
        scoreBoard.setStyle(
                "-fx-font-size: x-large; -fx-background-color: wheat; -fx-border-radius: 10 10 10 10;" +
                        " -fx-background-radius:  10 10 10 10; -fx-border-color: black;");
        windDegreeLabel.setStyle(
                "-fx-font-size: x-large; -fx-background-color: wheat; -fx-border-radius: 10 10 10 10;" +
                        " -fx-background-radius:  10 10 10 10; -fx-border-color: black;");
        scoreBoard.setLayoutX(centerX - 25);
        scoreBoard.setLayoutY(15);
        windDegreeLabel.setLayoutY(15);
        windDegreeLabel.setLayoutX(centerX + 85);
        ballsLeftLabel.setLayoutX(centerX + 30);
        windDegreeLabel.setAlignment(Pos.CENTER);
        ballsLeftLabel.setLayoutY(15);
        windDegreeLabel.setMinWidth(50);
        timePassedLabel.setLayoutY(15);
        scoreBoard.setMinWidth(50);
        ballsLeftLabel.setMinWidth(50);
        ballsLeftLabel.setAlignment(Pos.CENTER);
        scoreBoard.setAlignment(Pos.CENTER);
        timePassedLabel.setLayoutX(centerX - 75);
        gamePane.getChildren().addAll(scoreBoard, ballsLeftLabel, timePassedLabel, windDegreeLabel);
        //TODO add top labels to pane
    }

    private void addMainWatchDog() {
        changeListener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                updateTimePassed();

                if (targetCircle != null && (targetCircle.getCenterX() <= 0 || targetCircle.getCenterX() >= paneWidth)) {
                    lostTheGame();
                    rotate.angleProperty().removeListener(changeListener);
                    return;
                }

                if (targetCircle != null && getDistanceFromCenter(targetCircle) <= orbitRadius) {
                    straightLineMotion.stop();

                    gamePane.getChildren().remove(targetCircle);
                    putCircleToExistingCircles(getSmallCircleToBeAdded(targetCircle.getCenterX(), targetCircle.getCenterY()), ballsLeftToThrow);

                    if (phase2Animation == null && checkIntersection()) {
                        lostTheGame();
                        rotate.angleProperty().removeListener(changeListener);
                        return;
                    }

                    addScore(5);//TODO improve score protocol
                    decrementBallsLeft();

                    if (ballsLeftToThrow == (3 * totalBallsToThrow) / 4)
                        initiatePhase2Animations();
                    else if (ballsLeftToThrow == (totalBallsToThrow / 2))
                        initiatePhase3Animations();
                    else if (ballsLeftToThrow == (totalBallsToThrow / 4))
                        initiatePhase4Animations();

                    if (ballsLeftToThrow == 0) {
                        //TODO link to end game caller
                        wonTheGame();
                        rotate.angleProperty().removeListener(changeListener);
                    }

                    targetCircle = null;
                }
                else if (timePassed - TotalTime > 0.2) {
                    //TODO link to end game caller
                    lostTheGame();
                    rotate.angleProperty().removeListener(changeListener);
                }
            }
        };

        rotate.angleProperty().addListener(changeListener);
    }

    private void initiatePhase4Animations() {
        initiateBallDisplacementHandlers();
        addTransitionForWindDegree();
    }

    private void initiateBallDisplacementHandlers() {
        gamePane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                KeyCode keyCode = keyEvent.getCode();
                double displacement = 8.0;

                if (keyCode.equals(KeyCode.LEFT) && (reserveCircleX >= centerX - orbitRadius + displacement)) {
                    reserveCircleX -= displacement;
                    if (reserveCircle != null)
                        reserveCircle.setCenterX(reserveCircle.getCenterX() - displacement);
                }
                else if (keyCode.equals(KeyCode.RIGHT) && (reserveCircleX <= centerX + orbitRadius - displacement)) {
                    reserveCircleX += displacement;
                    if (reserveCircle != null)
                        reserveCircle.setCenterX(reserveCircle.getCenterX() + displacement);
                }

            }
        });
        gamePane.requestFocus();
    }

    private void addTransitionForWindDegree() {
        windSpeedTransition = new WindSpeedTransition(this, windSpeed);
        windSpeedTransition.play();
    }

    private void initiatePhase3Animations() {
        phase3Animation = new Phase3Animation(circles, lines, texts);
        phase3Animation.play();
    }

    private void wonTheGame() {
        stopAnimations();
        gamePane.setStyle("-fx-background-color: #0cf50c");
        timeline.stop();
//        circles.getTransforms().clear();
//        lines.getTransforms().clear();
//        texts.getTransforms().clear();
        GameWon gameWon = new GameWon(circles, texts, lines);
        gameWon.play();

        gameWon.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                rotate.angleProperty().removeListener(changeListener);
                Alert alert = showPopUp("YOU WON!");
                alert.setOnCloseRequest(new EventHandler<DialogEvent>() {
                    @Override
                    public void handle(DialogEvent dialogEvent) {
                        try {
                            new LoginMenu().start(new Stage());
                            //TODO change for main menu
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        });
    }

    private void lostTheGame() {
        stopAnimations();
        gamePane.setStyle("-fx-background-color: red");
        timeline.pause();
//        circles.getTransforms().clear();
//        lines.getTransforms().clear();
//        texts.getTransforms().clear();

        if (reserveCircle != null)
            gamePane.getChildren().remove(reserveCircle);
        if (targetCircle != null)
            gamePane.getChildren().remove(targetCircle);

        GameLost gameLost = new GameLost(circles, texts, lines, rotate.getAngle());
        gameLost.play();
        gameLost.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                rotate.angleProperty().removeListener(changeListener);
                Alert alert = showPopUp("YOU LOST!");
                alert.setOnCloseRequest(new EventHandler<DialogEvent>() {
                    @Override
                    public void handle(DialogEvent dialogEvent) {
                        try {
                            new LoginMenu().start(new Stage());
                            //TODO change for main menu
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

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

    private void addInternalWatchDog() {
        rotate.angleProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                if (checkIntersection()) {
                    lostTheGame();
                    rotate.angleProperty().removeListener(changeListener);
                }
            }
        });
    }

    private void initiatePhase2Animations() {
        phase2Animation = new Phase2Animation(circles, smallCircleRadius);
        phase2Animation.play();
        addRandomDirectionReverse();
        addInternalWatchDog();
    }

    private void addRandomDirectionReverse() {
        rotate.angleProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                if ((timePassed - lastDirectionReverseTime >= (4.0 + rand.nextDouble(0.7,4.0))
                        && (rand.nextBoolean()))){
                    setOmega(-omega);
                    lastDirectionReverseTime = timePassed;
                }
            }
        });
    }

    private void setOmega(double newOmega) {
        timeline.pause();
        this.omega = newOmega;
        setTimeLine();
        timeline.play();
    }

    private void stopAnimations() {
        if (phase2Animation != null)
            phase2Animation.stop();
        if (phase3Animation != null)
            phase3Animation.stop();
        if (windSpeedTransition != null)
            windSpeedTransition.stop();
        circles.setOpacity(1);
        texts.setOpacity(1);
        lines.setOpacity(1);
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

    private Text getText(Integer number, double startX, double startY) {
        Text text = new Text((number == null) ? " " : Integer.toString(number));
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
        timePassedLabel.setText(((((int) (timePassed)) / 60) <= 9 ? "0" : "") +
                (int) (timePassed) / 60 +
                ":" +
                ((int) (timePassed % 60) <= 9 ? "0" : "") + (int) (timePassed % 60));
    }

    public int getScore() {
        return score;
    }

    private Alert showPopUp(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(TextBase.getCurrentText(message));
        alert.setHeaderText(TextBase.getCurrentText(message));
        ((Button) alert.getDialogPane().lookupButton(ButtonType.OK)).setText("back to main menu");
        //TODO fix level
        alert.setContentText(TextBase.getCurrentText("score") + ": " + score
                + "\n\n" + TextBase.getCurrentText("time") + ": " + (int) timePassed
                + "\n\n" + TextBase.getCurrentText("level") +  ": " + 0);
        alert.show();
        return alert;
    }

    private double getDistanceFromCenter(Circle circle) {
        return Math.sqrt(Math.pow(circle.getCenterX() - centerX, 2) + Math.pow(circle.getCenterY() - centerY, 2));
    }

    private boolean checkIntersection() {
        for (int i = 0; i < circles.getChildren().size() - 1; i++)
            if (circles.getChildren().get(i).intersects(circles.getChildren().get(circles.getChildren().size() - 1).getBoundsInParent())) {
                return true;
            }
        return false;
    }

    public static double getCenterX() {
        return centerX;
    }

    public static double getCenterY() {
        return centerY;
    }

    public void setWindDegree(double amount) {
        windDegree = amount;
        windDegreeLabel.setText(Double.toString(windDegree));
    }

    public double getWindDegree() {
        return windDegree;
    }
}