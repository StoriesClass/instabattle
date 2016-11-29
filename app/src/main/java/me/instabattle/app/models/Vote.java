package me.instabattle.app.models;

import me.instabattle.app.managers.EntryManager;
import me.instabattle.app.managers.VoteManager;

public class Vote {
    private int id;
    private int firstEntryId;
    private int secondEntryId;

    public Entry getFirstEntry() {
        return EntryManager.getEntryById(firstEntryId);
    }

    public Entry getSecondEntry() {
        return EntryManager.getEntryById(secondEntryId);
    }

    public boolean voteForFirst() {
        return VoteManager.voteForFirst(id);
    }

    public boolean voteForSecond() {
        return VoteManager.voteForSecond(id);
    }
}
