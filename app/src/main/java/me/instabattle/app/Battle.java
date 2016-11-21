package me.instabattle.app;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wackloner on 21.11.2016.
 */

public class Battle {
    private String name;
    private LatLng location;
    private int radius;
    private List<Entry> entries;

    public Battle(String name, double lat, double lng) {
        this.name = name;
        this.location = new LatLng(lat, lng);
        entries = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public LatLng getLocation() {
        return location;
    }

    public int getRadius() {
        return radius;
    }

    public List<Entry> getEntries() {
        return entries;
    }
}
