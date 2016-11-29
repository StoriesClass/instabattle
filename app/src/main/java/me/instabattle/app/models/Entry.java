package me.instabattle.app.models;

import android.graphics.Bitmap;

/**
 * Created by wackloner on 21.11.2016.
 */

public class Entry {
    private User author;
    private Bitmap photo;
    private int upvotes;
    private int id;
    private Battle battle;

    public Entry(User author, Bitmap photo) {
        this.author = author;
        this.photo = photo;
        this.upvotes = 0;
        author.addEntry(this);
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public User getAuthor() {
        return author;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public int getId() {
        return id;
    }

    public Battle getBattle() {
        return battle;
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void upvote() {
        upvotes++;
    }
}
