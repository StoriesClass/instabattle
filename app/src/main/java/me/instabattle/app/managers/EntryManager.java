package me.instabattle.app.managers;

import java.util.Arrays;
import java.util.List;

import me.instabattle.app.models.Entry;

public class EntryManager {
    public static List<Entry> getEntriesByBattle(int battleId, int firstEntryNum, int entriesCount) {
        //TODO: send http request to get json with battles entries [firstEntryNum, firstEntryNum + entriesCount)
        //TODO: make entries from json
        if (battleId == 0) {
            return examples.subList(0, 4);
        } else if (battleId == 1) {
            return examples.subList(4, 10);
        } else {
            return null;
        }
    }

    public static List<Entry> getEntriesByUser(int userId, int firstEntryNum, int entriesCount) {
        //TODO: send http request to get json with users entries [firstEntryNum, firstEntryNum + entriesCount)
        //TODO: make entries from json
        if (userId == 0) {
            return Arrays.asList(examples.get(0), examples.get(4));
        } else if (userId == 1) {
            return Arrays.asList(examples.get(1), examples.get(5));
        } else if (userId == 2) {
            return Arrays.asList(examples.get(2), examples.get(6));
        } else if (userId == 3) {
            return Arrays.asList(examples.get(3), examples.get(7));
        } else if (userId == 4) {
            return Arrays.asList(examples.get(8));
        } else if (userId == 5) {
            return Arrays.asList(examples.get(9));
        } else {
            return null;
        }
    }

    public static Entry getEntryById(int entryId) {
        //TODO: bla-bla-bla
        if (entryId < examples.size()) {
            return examples.get(entryId);
        } else {
            return null;
        }
    }

    private static List<Entry> examples = Arrays.asList(
            new Entry(0, 0, 0, 100500),
            new Entry(1, 0, 1, 1337),
            new Entry(2, 0, 2, 1234),
            new Entry(3, 0, 3, 900),
            new Entry(4, 1, 0, 100500),
            new Entry(5, 1, 1, 1234),
            new Entry(6, 1, 2, 234),
            new Entry(7, 1, 3, 2345),
            new Entry(8, 1, 4, 3456),
            new Entry(9, 1, 5, 9000)
    );
}
