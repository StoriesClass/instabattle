package me.instabattle.app;

import android.util.Pair;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    public Pair<Entry, Entry> getNewPairForVote() {
        Random rnd = new Random(System.nanoTime());
        int n = entries.size(), i = 0, j = 0;
        while (i == j) {
            i = rnd.nextInt(n);
            j = rnd.nextInt(n);
        }
        return new Pair<>(entries.get(i), entries.get(j));
    }

    public void addNewVote(Entry winner, Entry looser) {
        winner.upvote();
    }
}
