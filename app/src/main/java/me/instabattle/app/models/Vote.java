package me.instabattle.app.models;

import me.instabattle.app.managers.EntryManager;
import me.instabattle.app.managers.VoteManager;
import retrofit2.Callback;

public class Vote {
    private int id;
    private int firstEntryId;
    private int secondEntryId;

    public void getFirstEntryAndDo(Callback<Entry> callback) {
        EntryManager.getEntryByIdAndDo(firstEntryId, callback);
    }

    public void getSecondEntryAndDo(Callback<Entry> callback) {
        EntryManager.getEntryByIdAndDo(secondEntryId, callback);
    }

    public boolean voteForFirst() {
        return VoteManager.voteForFirst(id);
    }

    public boolean voteForSecond() {
        return VoteManager.voteForSecond(id);
    }
}
