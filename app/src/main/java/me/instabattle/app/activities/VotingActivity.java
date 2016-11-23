package me.instabattle.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Pair;
import android.view.View;

import me.instabattle.app.Entry;
import me.instabattle.app.R;
import me.instabattle.app.State;
import me.instabattle.app.dialogs.VotingEndDialog;

public class VotingActivity extends Activity {

    private Entry first;
    private Entry second;

    private DialogFragment voteEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);

        voteEnd = new VotingEndDialog();

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
        voteEnd.show(getFragmentManager(), "voteEnd");
    }
}
