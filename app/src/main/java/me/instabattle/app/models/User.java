package me.instabattle.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import me.instabattle.app.managers.EntryManager;
import retrofit2.Callback;

public class User {
    @SerializedName("created_on")
    @Expose
    private String createdOn;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("rating")
    @Expose
    private Double rating;
    @SerializedName("username")
    @Expose
    private String username;

    //FIXME: made for example
    public User(String username) {
        this.username = username;
    }

    public void getEntriesAndDo(Callback<List<Entry>> callback) {
        EntryManager.getEntriesByUserAndDo(id, callback);
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
