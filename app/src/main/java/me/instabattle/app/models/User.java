package me.instabattle.app.models;

import java.util.List;

import me.instabattle.app.managers.EntryManager;

public class User {
    private int id;
    private String nickname;
    private int entriesCount;
    private int rating;

    public User(int id, String nickname, int entriesCount, int rating) {
        this.id = id;
        this.nickname = nickname;
        this.entriesCount = entriesCount;
        this.rating = rating;
    }

    public List<Entry> getEntries(int firstEntryNum, int entriesCount) {
        return EntryManager.getEntriesByUser(id, firstEntryNum, entriesCount);
    }

    public List<Entry> getEntries() {
        return getEntries(0, entriesCount);
    }

    public int getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }
}
