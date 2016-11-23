package me.instabattle.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

public class VotingActivity extends Activity {

    private Entry first;
    private Entry second;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);

        initNewVoting();
    }

    private void initNewVoting() {
        Pair<Entry, Entry> newVote = State.chosenBattle.getNewPairForVote();
        first = newVote.first;
        second = newVote.second;
    }

    public void vote(View v) {
        if (v.getId() == R.id.voteFirstBtn) {
            State.chosenBattle.addNewVote(first, second);
        } else {
            State.chosenBattle.addNewVote(second, first);
        }
    }
}
