package me.instabattle.app;

import android.util.Pair;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by wackloner on 21.11.2016.
 */

public class Battle {
    private String name;
    private LatLng location;
    private int radius;
    private List<Entry> entries;

    public Battle(String name, double lat, double lng) {
        this.name = name;
        this.location = new LatLng(lat, lng);
        entries = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public LatLng getLocation() {
        return location;
    }

    public int getRadius() {
        return radius;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public int getEntriesCount() {
        return entries.size();
    }

    public void addEntry(Entry entry) {
        entry.setId(entries.size());
        entries.add(entry);
    }

    public Pair<Entry, Entry> getNewPairForVote() {
        Random rnd = new Random(System.nanoTime());
        int n = entries.size(), i = 0, j = 0;
        while (i == j) {
            i = rnd.nextInt(n);
            j = rnd.nextInt(n);
        }
        return new Pair<>(entries.get(i), entries.get(j));
    }

    public void addNewVote(Entry winner, Entry looser) {
        winner.upvote();
        int winnerId = winner.getId(), swapId = winnerId;
        while (swapId > 0) {
            if (entries.get(swapId - 1).getUpvotes() >= winner.getUpvotes()) {
                break;
            }
            swapId--;
        }
        if (swapId != winnerId) {
            entries.get(swapId).setId(winnerId);
            winner.setId(swapId);
            entries.set(winnerId, entries.get(swapId));
            entries.set(swapId, winner);
        }
    }

    public Entry getWinner() {
        return entries.get(0);
    }
}
