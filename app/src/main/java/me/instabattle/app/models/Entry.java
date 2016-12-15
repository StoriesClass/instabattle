package me.instabattle.app.models;

import android.graphics.Bitmap;

import me.instabattle.app.managers.BattleManager;
import me.instabattle.app.managers.PhotoManager;
import me.instabattle.app.managers.UserManager;
import retrofit2.Callback;

public class Entry {
    private int id;
    private int authorId;
    private int battleId;
    private int rating;

    public Entry(int id, int battleId, int authorId, int rating) {
        this.id = id;
        this.authorId = authorId;
        this.battleId = battleId;
        this.rating = rating;
    }

    public Bitmap getPhoto() {
        return PhotoManager.getPhoto(id);
    }

    public void getAuthorAndDo(Callback<User> callback) {
        UserManager.getAndDo(authorId, callback);
    }

    public int getRating() {
        return rating;
    }

    public int getId() {
        return id;
    }

    public void getBattleAndDo(Callback<Battle> callback) {
        BattleManager.getBattleByIdAndDo(id, callback);
    }

    public void setId(int id) {
        this.id = id;
    }
}
