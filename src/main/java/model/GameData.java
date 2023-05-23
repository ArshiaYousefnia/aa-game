package model;

import java.util.ArrayList;
import java.util.HashMap;

public class GameData {
    private final int totalBallsToThrow;
    private int ballsLeftToThrow;
    private int score = 0;
    private double windDegree = 0;
    private double windSpeed = 1.5;
    private final double totalTime = 300.0;
    private double timePassed = 0.0;
    private double lastDirectionReverseTime = 0.0;
    private double omega = 5.0;
    private HashMap<Double, Integer> existingCircles = new HashMap<>();
    private double freezeBarProgress;
    private double freezeLengthSeconds = 3;

    public GameData(int totalBallsToThrow, int ballsLeftToThrow, int score, double windDegree, double windSpeed, double timePassed, double lastDirectionReverseTime, double omega, HashMap<Double, Integer> existingCircles, double freezeBarProgress, double freezeLengthSeconds) {
        this.totalBallsToThrow = totalBallsToThrow;
        this.ballsLeftToThrow = ballsLeftToThrow;
        this.score = score;
        this.windDegree = windDegree;
        this.windSpeed = windSpeed;
        this.timePassed = timePassed;
        this.lastDirectionReverseTime = lastDirectionReverseTime;
        this.omega = omega;
        this.existingCircles.putAll(existingCircles);
        this.freezeBarProgress = freezeBarProgress;
        this.freezeLengthSeconds = freezeLengthSeconds;
    }

    public GameData(GameData toBeCopied) {
        this.totalBallsToThrow = toBeCopied.totalBallsToThrow;
        this.ballsLeftToThrow = toBeCopied.ballsLeftToThrow;
        this.score = toBeCopied.score;
        this.windDegree = toBeCopied.windDegree;
        this.windSpeed = toBeCopied.windSpeed;
        this.timePassed = toBeCopied.timePassed;
        this.lastDirectionReverseTime = toBeCopied.lastDirectionReverseTime;
        this.omega = toBeCopied.omega;
        this.existingCircles.putAll(toBeCopied.existingCircles);
        this.freezeBarProgress = toBeCopied.freezeBarProgress;
        this.freezeLengthSeconds = toBeCopied.freezeLengthSeconds;
    }

    public static GameData getNewGameGameData(int difficultyLevel, ArrayList<Double> initialCircles, int totalBallsToThrow) {
        int ballsLeftToThrow = totalBallsToThrow;
        int score = 0;
        double windDegree = 0;
        double timePassed = 0;
        double lastDirectionReverseTime = 0;
        double freezeBarProgress = 0;
        HashMap<Double, Integer> existingCircles = new HashMap<>();
        double windSpeed = 2;
        double omega = 6;
        double freezeLengthSeconds = 5;

        for (double degree : initialCircles)
            existingCircles.put(degree, null);

        switch (difficultyLevel) {
            case 1:
                windSpeed = 1;
                omega = 11;
                freezeLengthSeconds = 7;
                break;
            case 3:
                windSpeed = 3;
                omega = 4;
                freezeLengthSeconds = 3;
                break;
            default:
                break;
        }

        return new GameData(totalBallsToThrow, ballsLeftToThrow, score, windDegree, windSpeed, timePassed, lastDirectionReverseTime, omega, existingCircles, freezeBarProgress, freezeLengthSeconds);
    }

    public int getTotalBallsToThrow() {
        return totalBallsToThrow;
    }

    public int getBallsLeftToThrow() {
        return ballsLeftToThrow;
    }

    public int getScore() {
        return score;
    }

    public double getWindDegree() {
        return windDegree;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public double getTotalTime() {
        return totalTime;
    }

    public double getTimePassed() {
        return timePassed;
    }

    public double getLastDirectionReverseTime() {
        return lastDirectionReverseTime;
    }

    public double getOmega() {
        return omega;
    }

    public HashMap<Double, Integer> getExistingCircles() {
        return existingCircles;
    }

    public double getFreezeBarProgress() {
        return freezeBarProgress;
    }

    public double getFreezeLengthSeconds() {
        return freezeLengthSeconds;
    }
}