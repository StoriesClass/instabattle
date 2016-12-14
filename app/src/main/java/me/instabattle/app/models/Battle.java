package me.instabattle.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import me.instabattle.app.State;
import me.instabattle.app.managers.EntryManager;
import me.instabattle.app.managers.VoteManager;
import retrofit2.Callback;

public class Battle {
    private LatLng location = null;
    private int radius;
    private int entriesCount = 0;
    private int winnerId;

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

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LatLng getLocation() {
        //FIXME: need to get LatLng from JsonConverter
        if (location == null) {
            location = new LatLng(this.latitude, this.longitude);
        }
        return location;
    }

    public int getRadius() {
        return radius;
    }

    public void getEntriesAndDo(Callback<List<Entry>> callback) {
        EntryManager.getEntriesByBattleAndDo(id, callback);
    }

    public int getEntriesCount() {
        return entriesCount;
    }

    public void getWinnerAndDo(Callback<Entry> callback) {
        EntryManager.getEntryByIdAndDo(winnerId, callback);
    }

    public Vote getVote() {
        return VoteManager.getVote(id, State.currentUser.getId());
    }
}
