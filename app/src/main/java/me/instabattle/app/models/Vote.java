package me.instabattle.app.models;

import me.instabattle.app.managers.VoteManager;

public class Vote {
    int id;
    private Entry firstEntry;
    private Entry secondEntry;

    public Entry getFirstEntry() {
        return firstEntry;
    }

    public Entry getSecondEntry() {
        return secondEntry;
    }

    public boolean voteForFirst() {
        return VoteManager.voteForFirst(id);
    }

    public boolean voteForSecond() {
        return VoteManager.voteForSecond(id);
    }
}
