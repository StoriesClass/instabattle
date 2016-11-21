package me.instabattle.app;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by wackloner on 21.11.2016.
 */

public class Battle {
    private String name;
    private LatLng location;

    public Battle(String name, double lat, double lng) {
        this.name = name;
        this.location = new LatLng(lat, lng);
    }

    public String getName() {
        return name;
    }

    public LatLng getLocation() {
        return location;
    }
}
