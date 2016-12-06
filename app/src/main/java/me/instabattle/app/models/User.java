package me.instabattle.app.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import me.instabattle.app.managers.EntryManager;

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

    public List<Entry> getEntries() {
        return EntryManager.getEntriesByUser(id);
    }

    public int getId() {
        return id;
    }

    public String getNickname() {
        return username;
    }
}
