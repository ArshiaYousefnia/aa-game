package model;

import com.google.common.hash.Hashing;
import javafx.scene.image.Image;

import java.nio.charset.StandardCharsets;

public class User {
    private String username;
    private String password;
    private String avatarPath;
    private int highScore;
    private int time;
    private int level;

    public User(String username, String password, String avatarPath) {
        this.username = username;
        this.password = encryptString(password);
        this.avatarPath = avatarPath;
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

    private String encryptString(String input) {
        return Hashing.sha256()
                .hashString(input, StandardCharsets.UTF_8)
                .toString();
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
}