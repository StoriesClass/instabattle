package me.instabattle.app.models;

import android.graphics.Bitmap;

import me.instabattle.app.managers.BattleManager;
import me.instabattle.app.managers.PhotoManager;
import me.instabattle.app.managers.UserManager;

public class Entry {
    private int id;
    private int authorId;
    private int battleId;
    private int upvotes;

    public Bitmap getPhoto() {
        return PhotoManager.getPhoto(id);
    }

    public User getAuthor() {
        return UserManager.getUserById(authorId);
    }

    public int getUpvotes() {
        return upvotes;
    }

    public int getId() {
        return id;
    }

    public Battle getBattle() {
        return BattleManager.getBattleById(battleId);
    }

    public void setId(int id) {
        this.id = id;
    }
}
