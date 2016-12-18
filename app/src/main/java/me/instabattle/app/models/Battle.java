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
    private Integer radius;
    @SerializedName("entry_count")
    @Expose
    private Integer entriesCount = 0;
    @SerializedName("created_on")
    @Expose
    private Date createdOn;
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
    @SerializedName("user_id")
    @Expose
    private Integer authorId;

    // FIXME constructor without description
    public Battle(Integer authorId, String name, String description,
                  Double lat, Double lng, Integer radius) {
        this.authorId = authorId;
        this.name = name;
        this.description = description;
        this.location = new LatLng(lat, lng);
        this.latitude = lat;
        this.longitude = lng;
        this.radius = radius;

        this.creatorId = State.currentUser.getId();
        this.entriesCount = 1;
        this.createdOn = new Date();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Date getCreatedOn() {
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
        //FIXME return radius;
        return 2000000; // for example
    }

    public void getEntriesAndDo(Callback<List<Entry>> callback) {
        EntryManager.getByBattleAndDo(id, callback);
    }

    public int getEntriesCount() {
        return entriesCount;
    }

    public void getWinnerAndDo(Callback<List<Entry>> callback) {
        EntryManager.getTopByBattleAndDo(winnerId, 1, callback);
    }

    public void getVoteAndDo(Callback<List<Entry>> callback) {
        EntryManager.getVoteAndDo(id, callback);
    }

    public void createEntryAndDo(Callback<Entry> callback) {
        //TODO: send photo
        EntryManager.createAndDo(id, State.currentUser.getId(), callback);
    }

    public Integer getId() {
        return id;
    }
}
