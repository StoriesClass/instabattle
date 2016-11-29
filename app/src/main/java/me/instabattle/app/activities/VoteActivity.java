package me.instabattle.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.DialogFragment;
import android.view.View;
import android.widget.ImageView;

import me.instabattle.app.R;
import me.instabattle.app.State;
import me.instabattle.app.dialogs.VotingEndDialog;
import me.instabattle.app.models.Vote;

public class VoteActivity extends Activity {

    private Vote currentVote;

    private DialogFragment voteEnd;

    private ImageView firstImage;
    private ImageView secondImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        voteEnd = new VotingEndDialog();

        firstImage = (ImageView) findViewById(R.id.firstImage);
        secondImage = (ImageView) findViewById(R.id.secondImage);

        initNewVoting();
    }

    public void initNewVoting() {
        currentVote = State.chosenBattle.getVote();
        firstImage.setImageBitmap(currentVote.getFirstEntry().getPhoto());
        firstImage.invalidate();
        secondImage.setImageBitmap(currentVote.getSecondEntry().getPhoto());
        secondImage.invalidate();
    }

    public void vote(View v) {
        if (v.getId() == R.id.voteFirstBtn) {
            currentVote.voteForFirst();
        } else {
            currentVote.voteForSecond();
        }
        voteEnd.show(getFragmentManager(), "voteEnd");
    }

    @Override
    public void onBackPressed() {
        Intent voting = new Intent(this, BattleActivity.class);
        startActivity(voting);
    }
}
