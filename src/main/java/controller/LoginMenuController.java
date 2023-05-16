package controller;

import model.User;

public class LoginMenuController {
    public String logIn(String username, String password) {
        String output;
        if ((output = checkEmptyField(username, password)) != null)
            return output;
        User user;
        if ((user = DataBase.getUserByUsername(username)) == null)
            return "username not found!";
        if (!user.isPasswordCorrect(password))
            return "password and username don't match!";

        DataBase.setCurrentUser(user);
        return "successfully logged in!";
    }

    public String signUp(String username, String password) {
        String messageToBeDelivered;
        if ((messageToBeDelivered = checkEmptyField(username, password)) != null)
            return messageToBeDelivered;
        if (DataBase.getUserByUsername(username) != null)
            return "username unavailable!";
        if ((messageToBeDelivered = checkUsername(username)) != null)
            return messageToBeDelivered;
        if ((messageToBeDelivered = checkPassword(password)) != null)
            return messageToBeDelivered;

        DataBase.addUser(User.getNewUser(username, password));
        return "user signed up successfully!";
    }

    private String checkPassword(String password) {
        if (password.length() < 5)
            return "password length must be at least 5!";
        if (password.length() > 15)
            return "password length must be at most 15!";
        if (!password.matches(".*[0-9]+.*"))
            return "password must contain numbers!";
        if (!password.matches(".*[-@#$%^&*!]+.*"))
            return "password must contain special characters(-@#$%^&*!)!";
        if (!password.matches(".*[a-z]+.*"))
            return "password must contain lowercase letters!";
        if (!password.matches(".*[A-Z]+.*"))
            return "password must contain uppercase letters!";

        return null;
    }

    private String checkUsername(String username) {
        if (username.length() < 5 || username.length() > 15)
            return "username length should between 5 and 15!";
        if (!username.startsWith("@"))
            return "username must start with @!";
        if (username.matches("^@.*[^a-z0-9]+.*$"))
            return "username must only have lowercase letters and numbers after @";
        if (username.matches("^@[^a-z]+.*$"))
            return "username must have a lowercase letter after @";

        return null;
    }

    private String checkEmptyField(String username, String password) {
        if (username.isEmpty())
            return "username field is empty!";
        if (password.isEmpty())
            return "password field is empty!";

        return null;
    }
}