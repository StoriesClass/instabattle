package me.instabattle.app.models;

import java.util.List;

import me.instabattle.app.managers.EntryManager;

public class User {
    private int id;
    private String nickname;
    private int entriesCount;

    public User(String nickname) {
        this.nickname = nickname;
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
