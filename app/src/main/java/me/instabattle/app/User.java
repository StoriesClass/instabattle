package me.instabattle.app;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wackloner on 23.11.2016.
 */

public class User {
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

    public String getNickname() {
        return nickname;
    }
}
