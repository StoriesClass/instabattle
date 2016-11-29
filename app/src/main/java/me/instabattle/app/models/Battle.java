package me.instabattle.app.models;


import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import me.instabattle.app.State;
import me.instabattle.app.managers.EntryManager;
import me.instabattle.app.managers.VoteManager;

public class Battle {
    private int id;
    private String name;
    private String desctiption;
    private LatLng location;
    private int radius;
    private int entriesCount;

    public Battle(String name, LatLng location, int entriesCount) {
        this.name = name;
        this.location = location;
        this.entriesCount = entriesCount;
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

    public List<Entry> getEntries(int firstEntryNum, int entriesCount) {
        return EntryManager.getEntriesByBattle(id, firstEntryNum, entriesCount);
    }

    public List<Entry> getEntries() {
        return getEntries(0, entriesCount);
    }

    public int getEntriesCount() {
        return entriesCount;
    }

    public Entry getWinner() {
        return getEntries(0, 1).get(0);
    }

    public Vote getVote() {
        return VoteManager.getVote(id, State.currentUser.getId());
    }
}
