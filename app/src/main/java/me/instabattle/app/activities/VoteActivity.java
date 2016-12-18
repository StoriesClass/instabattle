package me.instabattle.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

import me.instabattle.app.R;
import me.instabattle.app.managers.EntryManager;
import me.instabattle.app.settings.State;
import me.instabattle.app.dialogs.VotingEndDialog;
import me.instabattle.app.models.Entry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VoteActivity extends Activity {

    private static final String TAG = "VoteActivity";

    private DialogFragment voteEnd;

    private ImageView firstImage;
    private ImageView secondImage;

    private Entry firstEntry;
    private Entry secondEntry;

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
        State.chosenBattle.getVoteAndDo(new Callback<List<Entry>>() {
            @Override
            public void onResponse(Call<List<Entry>> call, Response<List<Entry>> response) {
                firstEntry = response.body().get(0);
                secondEntry = response.body().get(1);

                firstImage.setImageBitmap(firstEntry.getPhoto());
                firstImage.invalidate();

                secondImage.setImageBitmap(secondEntry.getPhoto());
                secondImage.invalidate();
            }

            @Override
            public void onFailure(Call<List<Entry>> call, Throwable t) {
                //TODO
                Log.e(TAG, "cant get vote: " + t);
            }
        });
    }

    public void vote(View v) {
        int winnerId, looserId;
        if (v.getId() == R.id.voteFirstBtn) {
            winnerId = firstEntry.getId();
            looserId = secondEntry.getId();
        } else {
            winnerId = secondEntry.getId();
            looserId = firstEntry.getId();
        }

        EntryManager.voteAndDo(State.chosenBattle.getId(), State.currentUser.getId(), winnerId, looserId, new Callback<List<Entry>>() {
            @Override
            public void onResponse(Call<List<Entry>> call, Response<List<Entry>> response) {
                //TODO
            }

            @Override
            public void onFailure(Call<List<Entry>> call, Throwable t) {
                //TODO
            }
        });

        voteEnd.show(getFragmentManager(), "voteEnd");
    }

    @Override
    public void onBackPressed() {
        Intent voting = new Intent(this, BattleActivity.class);
        startActivity(voting);
    }
}
