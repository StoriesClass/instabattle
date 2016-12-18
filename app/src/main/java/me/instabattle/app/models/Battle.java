package me.instabattle.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.List;

import me.instabattle.app.managers.EntryManager;
import me.instabattle.app.settings.State;
import retrofit2.Callback;

public class Battle {
    private LatLng location = null;
    private int winnerId;

    @SerializedName("radius")
    @Expose
    private int radius;
    @SerializedName("entry_count")
    @Expose
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

    public Battle(String name, String description, LatLng location, int radius) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.latitude = location.latitude;
        this.longitude = location.longitude;
        this.radius = radius;

        this.creatorId = State.currentUser.getId();
        this.entriesCount = 1;
        this.createdOn = (new Date()).toString();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public LatLng getLocation() {
        //FIXME: need to get LatLng from JsonConverter
        if (location == null) {
            location = new LatLng(this.latitude, this.longitude);
        }
        return location;
    }

    public int getRadius() {
        //FIXME pls
        return 500000;
    }

    public void getEntriesAndDo(Callback<List<Entry>> callback) {
        EntryManager.getByBattleAndDo(id, callback);
    }

    public int getEntriesCount() {
        return entriesCount;
    }

    public void getWinnerAndDo(Callback<Entry> callback) {
        EntryManager.getAndDo(winnerId, callback);
    }

    public void getVoteAndDo(Callback<List<Entry>> callback) {
        EntryManager.getVoteAndDo(id, callback);
    }

    public void createEntryAndDo(byte[] photo, Callback<Entry> callback) {
        //FIXME date format
        //TODO: sent photo
        EntryManager.createAndDo(id, State.currentUser.getId(), (new Date()).toString(), photo, callback);
    }
}
