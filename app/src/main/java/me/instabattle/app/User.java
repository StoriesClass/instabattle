package me.instabattle.app;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private String nickname;
    private List<Entry> entries;

    public User(String nickname) {
        this.nickname = nickname;
        entries = new ArrayList<>();
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void addEntry(Entry entry) {
        entries.add(entry);
    }

    public int getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }
}
