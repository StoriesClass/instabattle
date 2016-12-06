package me.instabattle.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import me.instabattle.app.State;
import me.instabattle.app.managers.EntryManager;
import me.instabattle.app.managers.VoteManager;

public class Battle {
    private LatLng location;
    private int radius;
    private int entriesCount = 0;

    @SerializedName("created_on")
    @Expose
    private String createdOn;
    @SerializedName("creator_id")
    @Expose
    private Integer creatorId;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("name")
    @Expose
    private String name;

    public Battle(int id, String name, LatLng location, int entriesCount) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.entriesCount = entriesCount;
    }

    public void init() {
        location = new LatLng(this.latitude, this.longitude);
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
