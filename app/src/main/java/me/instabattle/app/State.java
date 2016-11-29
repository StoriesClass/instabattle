package me.instabattle.app;

import com.google.android.gms.maps.model.LatLng;

import me.instabattle.app.models.Battle;
import me.instabattle.app.models.User;

/**
 * Created by wackloner on 23.11.2016.
 */

public class State {
    public static Battle chosenBattle;

    public static User currentUser = new User("wackloner");

    public static LatLng currentLocation;
}
