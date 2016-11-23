package me.instabattle.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.app.DialogFragment;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;

import me.instabattle.app.Entry;
import me.instabattle.app.R;
import me.instabattle.app.State;
import me.instabattle.app.dialogs.VotingEndDialog;

public class VotingActivity extends Activity {

    private Entry firstEntry;
    private Entry secondEntry;

    private DialogFragment voteEnd;

    private ImageView firstImage;
    private ImageView secondImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);

        voteEnd = new VotingEndDialog();

        firstImage = (ImageView) findViewById(R.id.firstImage);
        secondImage = (ImageView) findViewById(R.id.secondImage);

        initNewVoting();
    }

    public void initNewVoting() {
        Pair<Entry, Entry> newVote = State.chosenBattle.getNewPairForVote();
        firstEntry = newVote.first;
        secondEntry = newVote.second;
        firstImage.setImageBitmap(firstEntry.getPhoto());
        firstImage.invalidate();
        secondImage.setImageBitmap(secondEntry.getPhoto());
        secondImage.invalidate();
    }

    public void vote(View v) {
        if (v.getId() == R.id.voteFirstBtn) {
            State.chosenBattle.addNewVote(firstEntry, secondEntry);
        } else {
            State.chosenBattle.addNewVote(secondEntry, firstEntry);
        }
        voteEnd.show(getFragmentManager(), "voteEnd");
    }
}
