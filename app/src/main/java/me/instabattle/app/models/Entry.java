package me.instabattle.app.models;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import me.instabattle.app.managers.BattleManager;
import me.instabattle.app.managers.PhotoManager;
import me.instabattle.app.managers.UserManager;
import retrofit2.Callback;

public class Entry {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer authorId;
    @SerializedName("battle_id")
    @Expose
    private Integer battleId;
    @SerializedName("rating")
    @Expose
    private Double rating =25d;
    @SerializedName("created_on")
    @Expose
    private Date createdOn;
    @SerializedName("image")
    @Expose
    private String imageName;
    @SerializedName("latitude")
    @Expose
    private Double lat;
    @SerializedName("longitude")
    @Expose
    private Double lng;
    //FIXME
    private String description;


    public Entry(int battleId, int authorId) {
        this.authorId = authorId;
        this.battleId = battleId;
    }

    public Bitmap getPhoto() {
        return PhotoManager.getPhoto(id);
    }

    public void getAuthorAndDo(Callback<User> callback) {
        UserManager.getAndDo(authorId, callback);
    }

    public Double getRating() {
        return rating;
    }

    public Integer getId() {
        return id;
    }

    public void getBattleAndDo(Callback<Battle> callback) {
        BattleManager.getAndDo(id, callback);
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public Integer getBattleId() {
        return battleId;
    }

    public Integer getAuhtorId() {
        return authorId;
    }
}
