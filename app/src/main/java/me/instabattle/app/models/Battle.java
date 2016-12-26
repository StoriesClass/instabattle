package me.instabattle.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.List;

import me.instabattle.app.managers.EntryManager;
import me.instabattle.app.managers.UserManager;
import me.instabattle.app.settings.State;
import retrofit2.Callback;

public class Battle {
    @SerializedName("radius")
    @Expose
    private Integer radius;
    @SerializedName("entry_count")
    @Expose
    private Integer entriesCount = 0;
    @SerializedName("created_on")
    @Expose
    private Date createdOn;
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
    private Integer userId;


    public Battle(Integer userId, String name, String description,
                  Double lat, Double lng, Integer radius) {
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.latitude = lat;
        this.longitude = lng;
        this.radius = radius;

        this.userId = State.currentUser.getId();
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
        return new LatLng(latitude, longitude);
    }

    public int getRadius() {
        return radius;
    }

    public void getEntriesAndDo(Callback<List<Entry>> callback) {
        EntryManager.getByBattleAndDo(id, callback);
    }

    public int getEntriesCount() {
        return entriesCount;
    }

    public void getWinnerAndDo(Callback<List<Entry>> callback) {
        EntryManager.getTopByBattleAndDo(id, 1, callback);
    }

    public void getVoteAndDo(Callback<List<Entry>> callback) {
        EntryManager.getVoteAndDo(id, callback);
    }

    public void createEntryAndDo(Callback<Entry> callback) {
        //TODO: send photo
        EntryManager.createAndDo(id, State.currentUser.getId(), callback);
    }

    public void getAuthorAndDo(Callback<User> callback) {
        UserManager.getAndDo(userId, callback);
    }

    public Integer getId() {
        return id;
    }
}
