package me.instabattle.app;

import android.graphics.Bitmap;

/**
 * Created by wackloner on 21.11.2016.
 */

public class Entry {
    private User author;
    private Bitmap photo;
    private int upvotes;
    private int id;

    public Entry(User author, Bitmap photo) {
        this.author = author;
        this.photo = photo;
        this.upvotes = 0;
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

    public void setId(int id) {
        this.id = id;
    }

    public void upvote() {
        upvotes++;
    }
}
