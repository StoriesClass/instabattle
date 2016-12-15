package me.instabattle.app.settings;

import com.google.android.gms.maps.model.LatLng;

import me.instabattle.app.models.Battle;
import me.instabattle.app.models.User;

public class State {
    public static Battle chosenBattle;

    public static User currentUser = new User("wackloner");

    public static LatLng currentLocation;
}
