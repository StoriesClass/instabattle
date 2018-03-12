package me.instabattle.app.settings;

import me.instabattle.app.models.Battle;
import me.instabattle.app.models.User;

// FIXME
public class State {
    public static Battle chosenBattle;

    public static User currentUser = new User("wackloner");
    public static String token = null;

    public static boolean creatingBattle;
}
