package me.instabattle.app.managers;

import java.util.Arrays;
import java.util.List;

import me.instabattle.app.models.User;

public class UserManager {
    public static User getUserById(int userId) {
        //TODO: send http request to get json with user by id
        //TODO: make user from json
        if (userId < examples.size()) {
            return examples.get(userId);
        } else {
            return null;
        }
    }

    public static List<User> examples = Arrays.asList(
            new User(0, "wackloner", 2, 100500),
            new User(1, "Qumeric", 2, 420),
            new User(2, "glebwin", 2, 1337),
            new User(3, "egor_bb", 2, 666),
            new User(4, "tourist", 1, 3600),
            new User(5, "Miracle", 1, 9000)
    );
}
