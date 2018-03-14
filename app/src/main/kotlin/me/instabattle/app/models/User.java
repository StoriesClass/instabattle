package me.instabattle.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import me.instabattle.app.managers.EntryManager;
import retrofit2.Callback;

public class User {
    @SerializedName("created_on")
    @Expose // FIXME what is Expose actually?
    private Date createdOn;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("rating")
    @Expose
    private Double rating = 25d;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("battle_creation_limit")
    @Expose
    private Integer battleCreationLimit;

    //FIXME: made for example
    public User(String username) {
        this.username = username;
        this.id = 1;
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public void getEntriesAndDo(Callback<List<Entry>> callback) {
        EntryManager.INSTANCE.getByUserAndDo(id, callback);
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public int getBattleCreationLimit() {
        return battleCreationLimit;
    }
}
