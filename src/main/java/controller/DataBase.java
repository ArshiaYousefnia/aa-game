package controller;

import com.google.gson.Gson;
import model.User;
import view.LoginMenu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class DataBase {
    private static final String usersDataPath = "src/main/resources/data/users.json";
    private static final HashMap<String, User> users = new HashMap<>();
    private static User currentUser = null;
    private final static int defaultAvatarsCount = 7;
    private final static int minimumBallsToThrow = 30;
    private final static int maximumBallsToThrow = 80;
    private static  int ballsToThrow = 30;
    private static int difficulty = 2;
    private static boolean isLanguageEnglish = true;
    private static boolean soundStatus = false;
    private static String ballThrowKey = "space";
    private static String freezeKey = "tab";

    public static User getUserByUsername(String username) {
        return users.getOrDefault(username, null);
    }

    public static Object[] getAllUsersList() {
        return users.values().toArray();
    }

    public static void fetchUsers() throws FileNotFoundException {
        try {
            File dataFile = new File(usersDataPath);
            Scanner dataScanner = new Scanner(dataFile);
            Gson gson = new Gson();
            String output = dataScanner.nextLine();
            User[] fetchedUsers = gson.fromJson(output, User[].class);
            for (User user : fetchedUsers)
                users.put(user.getUsername(), user);
        } catch (Exception ignored) {
        }
    }

    public static void pushUsers() {
        try {
            FileWriter fileWriter = new FileWriter(usersDataPath);
            Gson gson = new Gson();
            String jsonToBePushed = gson.toJson(users.values().toArray());

            fileWriter.write(jsonToBePushed);
            fileWriter.close();
        } catch (Exception ignored) {
        }
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        DataBase.currentUser = currentUser;
    }

    public static int getDefaultAvatarsCount() {
        return defaultAvatarsCount;
    }

    public static void addUser(User user) {
        users.put(user.getUsername(), user);
    }

    public static void removeUser(User user) {
        users.remove(user.getUsername(), user);
    }

    public static String getFreezeKey() {
        return freezeKey;
    }

    public static int getDifficulty() {
        return difficulty;
    }

    public static boolean isSoundStatus() {
        return soundStatus;
    }

    public static String getBallThrowKey() {
        return ballThrowKey;
    }

    public static int getMinimumBallsToThrow() {
        return minimumBallsToThrow;
    }

    public static int getMaximumBallsToThrow() {
        return maximumBallsToThrow;
    }

    public static int getBallsToThrow() {
        return ballsToThrow;
    }

    public static void incrementBallsToThrow() {
        ballsToThrow++;
        if (ballsToThrow == maximumBallsToThrow + 1)
            ballsToThrow = minimumBallsToThrow;
    }

    public static void toggleSound() {
        soundStatus = !soundStatus;
    }

    public static String getLanguage() {
        return (isLanguageEnglish ? "English" : "فارسی");
    }

    public static void toggleLanguage() {
        isLanguageEnglish = !isLanguageEnglish;
    }

    public static boolean isLangEnglish() {
        return isLanguageEnglish;
    }

    public static void incrementDifficulty() {
        difficulty++;
        if (difficulty == 4)
            difficulty = 1;
    }
}