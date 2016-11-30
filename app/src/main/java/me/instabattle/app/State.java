package me.instabattle.app;

import com.google.android.gms.maps.model.LatLng;

import me.instabattle.app.managers.UserManager;
import me.instabattle.app.models.Battle;
import me.instabattle.app.models.User;

public class State {
    public static Battle chosenBattle;

    public static User currentUser = UserManager.examples.get(0);

    public static LatLng currentLocation;
}
