package model;

import com.google.common.hash.Hashing;
import controller.DataBase;
import javafx.scene.image.Image;

import java.nio.charset.StandardCharsets;
import java.util.Random;

public class User {
    private String username;
    private String password;
    private String avatarPath;
    private int defaultAvatarNumber;
    private int highScore;
    private int time;
    private int level;

    private User(String username, String password, String avatarPath, int number) {
        this.username = username;
        this.password = password;
        this.avatarPath = avatarPath;
        this.defaultAvatarNumber = number;
    }

    public static User getNewUser(String username, String password) {
        int number = getRandomAvatarNumber(DataBase.getDefaultAvatarsCount());
        String avatarPath = "/assets/avatars/default/"
                + number
                + ".jpg";
        return new User(username, encryptString(password), avatarPath, number);
    }

    private static String encryptString(String input) {
        return Hashing.sha256()
                .hashString(input, StandardCharsets.UTF_8)
                .toString();
    }

    private static int getRandomAvatarNumber(int totalNumber) {
        return ((new Random(5464)).nextInt(totalNumber)) + 1;
    }
    public String getUsername() {
        return username;
    }

    public Image getAvatar() {
        try {
            return new Image(User.class.getResource(avatarPath).toString());
        } catch (Exception ex) {
            return null;
        }
    }

    public int getHighScore() {
        return highScore;
    }

    public int getLevel() {
        return level;
    }

    public int getTime() {
        return time;
    }

    public boolean isPasswordCorrect(String toBeEvaluated) {
        return password.equals(encryptString(toBeEvaluated));
    }

    public void setPassword(String newPassword) {
        this.password = encryptString(newPassword);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isWorstThan(User userToCompareTo) {
        if (this.highScore < userToCompareTo.highScore)
            return true;
        if (this.highScore > userToCompareTo.highScore)
            return false;
        if (this.time > userToCompareTo.time)
            return true;
        if (this.time < userToCompareTo.time)
            return false;
//        if (this.level < userToCompareTo.level)
//            return true;
//        if (this.level > userToCompareTo.level)
//            return false;
        return this.username.compareTo(userToCompareTo.username) >= 0;
    }

    public int getDefaultAvatarNumber() {
        return defaultAvatarNumber;
    }

    public void toggleDefaultAvatarNumber(int total) {
        defaultAvatarNumber++;
        if (defaultAvatarNumber == total + 1)
            defaultAvatarNumber = 1;
        setAvatarPath("/assets/avatars/default/"
                + defaultAvatarNumber
                + ".jpg");
    }
}