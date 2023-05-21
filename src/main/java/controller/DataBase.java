package controller;

import com.google.gson.Gson;
import model.User;
import view.LoginMenu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.*;

public class DataBase {
    private static ArrayList<Double>[] startingBalls = new ArrayList[3];
    static {
        startingBalls[0] = new ArrayList<Double>() {
            {
                add(10.0);add(60.0);add(90.0);add(150.0);add(210.0);
            }
        };
        startingBalls[1] = new ArrayList<Double>() {
            {
                add(30.0);add(60.0);add(80.0);add(100.0);add(120.0);
            }
        };

        startingBalls[2] = new ArrayList<Double>() {
            {
                add(0.0);add(15.0);add(180.0);add(300.0);add(340.0);
            }
        };
    }
    private static final String usersDataPath = "src/main/resources/data/users.json";
    private static final HashMap<String, User> users = new HashMap<>();
    private static User currentUser = null;
    private final static int defaultAvatarsCount = 7;
    private final static int minimumBallsToThrow = 10;
    private final static int maximumBallsToThrow = 40;
    private static  int ballsToThrow = 30;
    private static int difficulty = 2;
    private static int startingBallsIndex = 0;
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

    public static int getStartingBallsIndex() {
        return startingBallsIndex;
    }

    public static void incrementStartingBallsIndex() {
        startingBallsIndex++;
        if (startingBallsIndex == startingBalls.length)
            startingBallsIndex = 0;
    }

    public static ArrayList<Double> getCurrentBalls() {
        return startingBalls[startingBallsIndex];
    }
}