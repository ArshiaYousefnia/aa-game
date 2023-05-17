package controller;

import model.User;

import java.util.ArrayList;

public class LeaderBoardsMenuController {
    private static int leaderBoarsSize = 10;
    private Object[] allObjects;
    private User[] allUsers;
    public User[] getSortedRanks(int levelToSortBY) {
        allObjects = DataBase.getAllUsersList();
        ArrayList<User> allUsers1 = new ArrayList<>();
        for ( int i = 0; i < allObjects.length; i++) {
            if (levelToSortBY == 0 || levelToSortBY == ((User) allObjects[i]).getLevel())
                allUsers1.add((User) allObjects[i]);
        }

        allUsers = new User[allUsers1.size()];
        for (int i = 0; i < allUsers.length; i++)
            allUsers[i] = allUsers1.get(i);

        if (allUsers.length == 1 && (levelToSortBY == 0 || levelToSortBY == allUsers[0].getLevel()))
            return allUsers;
        if (allUsers.length == 1)
            return new User[0];

        for (int mainIndex = 0; mainIndex < Math.min(leaderBoarsSize, allUsers.length); mainIndex++) {
            for (int i = allUsers.length - 2; i >= mainIndex; i--) {
                if (allUsers[i].isWorstThan(allUsers[i + 1]))
                    swapElements(i);
            }
        }

        return allUsers;
    }

    private void swapElements(int index) {
        User temporaryUser = allUsers[index];
        allUsers[index] = allUsers[index + 1];
        allUsers[index + 1] = temporaryUser;
    }

    public int getLeaderBoarsSize() {
        return leaderBoarsSize;
    }
}