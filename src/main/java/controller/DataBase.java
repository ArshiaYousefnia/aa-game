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
}