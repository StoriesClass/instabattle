package me.instabattle.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.DialogFragment;
import android.view.View;
import android.widget.ImageView;

import me.instabattle.app.R;
import me.instabattle.app.settings.State;
import me.instabattle.app.dialogs.VotingEndDialog;
import me.instabattle.app.models.Entry;
import me.instabattle.app.models.Vote;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        currentVote.getFirstEntryAndDo(new Callback<Entry>() {
            @Override
            public void onResponse(Call<Entry> call, Response<Entry> response) {
                firstImage.setImageBitmap(response.body().getPhoto());
                firstImage.invalidate();
            }

            @Override
            public void onFailure(Call<Entry> call, Throwable t) {
                //TODO
            }
        });
        currentVote.getSecondEntryAndDo(new Callback<Entry>() {
            @Override
            public void onResponse(Call<Entry> call, Response<Entry> response) {
                secondImage.setImageBitmap(response.body().getPhoto());
                secondImage.invalidate();
            }

            @Override
            public void onFailure(Call<Entry> call, Throwable t) {
                //TODO
            }
        });
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
