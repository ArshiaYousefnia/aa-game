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
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.DataPackage;
import model.GameData;
import view.model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GameApplicationController {
    private DataPackage dataPackage;
    private final double centralCircleRadius = 50;
    private final double  smallCircleRadius = 10;
    private static final double paneWidth = 600;
    private static final double paneHeight = 700;
    private static final double centerX = paneWidth / 2;
    private static final double centerY = paneHeight / 2;
    private final double orbitRadius = 200;
    private final double beginningPointDistanceFromCenter = 270;
    private ChangeListener changeListener;
    private int totalBallsToThrow = 15;
    private int ballsLeftToThrow = totalBallsToThrow;
    //TODO fix this
    private int score = 0;
    private double windDegree = 0;
    private double windSpeed = 1.5;
    private double totalTime = 300.0;
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
    private FreezeModeTransition freezeModeTransition;
    private WindSpeedTransition windSpeedTransition;
    private Random rand = new Random();
    private double omega = 5.0;
    private HashMap<Double, Integer> existingCircles = new HashMap<>();
    private Group lines = new Group();
    private Group circles = new Group();
    private Group texts = new Group();
    private Pane gamePane;
    private final Label ballsLeftLabel = new Label(Integer.toString(ballsLeftToThrow));
    private final Label scoreBoard = new Label(Integer.toString(score));
    private final Label timePassedLabel = new Label("0 : 0");
    private final Label windDegreeLabel = new Label(Double.toString(windDegree));
    private final ProgressBar freezeMode = new ProgressBar(0);
    private boolean isFreezed = false;
    private double freezeLengthSeconds = 3;

    private void loadRequirements(DataPackage dataPackage) {
        this.dataPackage = dataPackage;
        GameData gameData = dataPackage.getCurrentData();
        totalBallsToThrow = gameData.getTotalBallsToThrow();
        ballsLeftToThrow = gameData.getBallsLeftToThrow();
        score = gameData.getScore();
        windDegree = gameData.getWindDegree();
        windSpeed = gameData.getWindSpeed();
        totalTime = gameData.getTotalTime();
        timePassed = gameData.getTimePassed();
        lastDirectionReverseTime = gameData.getLastDirectionReverseTime();
        omega = gameData.getOmega();
        existingCircles.putAll(gameData.getExistingCircles());
        freezeMode.setProgress(gameData.getFreezeBarProgress());
        freezeLengthSeconds = gameData.getFreezeLengthSeconds();

        doPreparations();
    }

    private void doPreparations() {
        existingCircles.put(50.0, null);
        freezeMode.setProgress(0);
        ballsLeftToThrow++;
        decrementBallsLeft();
        updateTimePassed();
        addScore(score);
        setWindDegree(windDegree);
    }

    public Pane buildApp(DataPackage dataPackage) {
        gamePane = getPane();
        loadRequirements(dataPackage);
        loadGame();
        setRotation();
        setTimeLine();
        addRotationToGroups();
        addLabels();
        addProgressbar();
        gamePane.getChildren().addAll(circles, lines, texts, getNewCentralCircle(), centralCircle);
        addMainWatchDog();

        assignEventToPane();

        timeline.play();
        return gamePane;
    }

    private void assignEventToPane() {
        gamePane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                KeyCode keyCode = keyEvent.getCode();

                if (keyCode.equals(KeyCode.SPACE) && targetCircle == null && ballsLeftToThrow != 0) {
                    targetCircle = reserveCircle;

                    straightLineMotion = new StraightLineMotion(targetCircle, windDegree);
                    straightLineMotion.play();

                    reserveCircle = null;

                    if (ballsLeftToThrow > 1) {
                        reserveCircle = getSmallCircleForSetup(reserveCircleX, centerY + beginningPointDistanceFromCenter);
                        gamePane.getChildren().add(reserveCircle);
                    }
                }
                else if (keyCode.equals(KeyCode.TAB) && (freezeMode.getProgress() >= 0.99)) {
                    triggerFreezeMode();
                }
                else if (windSpeedTransition != null) {
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
                }
        });
    }

    private void triggerFreezeMode() {
        isFreezed = true;
        freezeMode.setProgress(0);
        freezeModeTransition = new FreezeModeTransition(this);
        freezeModeTransition.play();

        freezeModeTransition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                isFreezed = false;
            }
        });
    }

    private void loadGame() {
        centralCircle = getNewCentralCircle();
        checkEarlyPhaseTrigger();
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
        if (ballsLeftToThrow != 0) {
            reserveCircle = getSmallCircleForSetup(centerX, centerY + beginningPointDistanceFromCenter);
            gamePane.getChildren().add(reserveCircle);
        }
    }

    private void checkEarlyPhaseTrigger() {
        if (ballsLeftToThrow <= (3 * totalBallsToThrow) / 4)
            initiatePhase2Animations();
        if (ballsLeftToThrow <= (totalBallsToThrow / 2))
            initiatePhase3Animations();
        if (ballsLeftToThrow <= (totalBallsToThrow / 4))
            initiatePhase4Animations();
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

                    incrementFreezeBar();
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
                else if (timePassed - totalTime > 0.2) {
                    //TODO link to end game caller
                    lostTheGame();
                    rotate.angleProperty().removeListener(changeListener);
                }
            }
        };

        rotate.angleProperty().addListener(changeListener);
    }

    private void initiatePhase4Animations() {
        addTransitionForWindDegree();
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

    public void setOmega(double newOmega) {
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
    private Circle getNewCentralCircle() {
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

    private void addProgressbar() {
        setUpProgressBar();
        freezeMode.setLayoutY(20);
        freezeMode.setLayoutX(centerX - 160);
        gamePane.getChildren().add(freezeMode);
    }

    private void setUpProgressBar() {
        freezeMode.setMinWidth(70);
        freezeMode.setMaxWidth(70);
        freezeMode.setStyle("-fx-background-color: wheat; -fx-border-radius: 10 10 10 10;" +
        " -fx-background-radius:  10 10 10 10; -fx-border-color: black;");
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

    private void incrementFreezeBar() {
        if (isFreezed) return;
        double currentProgress = freezeMode.getProgress();
        if (currentProgress != 1) {
            currentProgress += 0.1;
            freezeMode.setProgress(currentProgress);
        }
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

    public void directFocus() {
        gamePane.requestFocus();
    }

    public double getFreefreezeLengthSeconds() {
        return freezeLengthSeconds;
    }

    public double getOmega() {
        return omega;
    }

    public Circle getCentralCircle() {
        return centralCircle;
    }

    private void updateDataPackage() {
        dataPackage.setCurrentData(new GameData(totalBallsToThrow, ballsLeftToThrow, score, windDegree, windSpeed, timePassed, lastDirectionReverseTime, omega, existingCircles, freezeMode.getProgress(), freezeLengthSeconds));
    }

    public DataPackage getDataPackage() {
        return dataPackage;
    }
}