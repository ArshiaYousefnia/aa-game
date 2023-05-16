package controller;

import com.google.gson.Gson;
import model.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Scanner;

public class DataBase {
    private static final String usersDataPath = "/data/users.json";
    private static final HashMap<String, User> users = new HashMap<>();

    public static User getUserByUsername(String username) {
        return users.getOrDefault(username, null);
    }

    public static void fetchUsers() {
        try {
            File dataFile = new File(DataBase.class.getResource(usersDataPath).toString());
            Scanner dataScanner = new Scanner(dataFile);

            Gson gson = new Gson();
            String output = dataScanner.nextLine();
            User[] fetchedUsers = gson.fromJson(output, User[].class);
            for (User user : fetchedUsers)
                users.put(user.getUsername(), user);
        } catch (FileNotFoundException ignored) {
        }
    }

    public static void pushUsers() {
        try {
            FileWriter fileWriter = new FileWriter(DataBase.class.getResource(usersDataPath).toString());
            Gson gson = new Gson();
            String jsonToBePushed = gson.toJson(users.values().toArray());

            fileWriter.write(jsonToBePushed);
            fileWriter.close();
        } catch (Exception ignored) {
        }
    }
}