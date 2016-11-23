package me.instabattle.app;

import android.graphics.Bitmap;

/**
 * Created by wackloner on 21.11.2016.
 */

public class Entry {
    private User author;
    private Bitmap photo;

    public Entry(User author, Bitmap photo) {
        this.author = author;
        this.photo = photo;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public User getAuthor() {
        return author;
    }
}
