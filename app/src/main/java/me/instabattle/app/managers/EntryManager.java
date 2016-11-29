package me.instabattle.app.managers;

import java.util.List;

import me.instabattle.app.models.Entry;

public class EntryManager {
    public static List<Entry> getEntriesByBattle(int battleId, int firstEntryNum, int entriesCount) {
        //TODO: send http request to get json with battles entries [firstEntryNum, firstEntryNum + entriesCount)
        //TODO: make entries from json
        return null;
    }

    public static List<Entry> getEntriesByUser(int userId, int firstEntryNum, int entriesCount) {
        //TODO: send http request to get json with users entries [firstEntryNum, firstEntryNum + entriesCount)
        //TODO: make entries from json
        return null;
    }
}
