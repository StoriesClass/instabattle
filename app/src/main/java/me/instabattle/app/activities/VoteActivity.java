package me.instabattle.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import me.instabattle.app.R;
import me.instabattle.app.managers.BitmapCallback;
import me.instabattle.app.managers.EntryManager;
import me.instabattle.app.models.Vote;
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

    public static Entry firstEntry;
    public static Entry secondEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        voteEnd = new VotingEndDialog();

        firstImage = (ImageView) findViewById(R.id.firstImage);
        secondImage = (ImageView) findViewById(R.id.secondImage);

        setPhotos();
    }

    public void setPhotos() {
        firstEntry.getPhotoAndDo(new BitmapCallback() {
            @Override
            public void onResponse(final Bitmap photo) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        firstImage.setImageBitmap(photo);
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, "can't get firstEntry photo");
            }
        });
        firstImage.invalidate();

        secondEntry.getPhotoAndDo(new BitmapCallback() {
            @Override
            public void onResponse(final Bitmap photo) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        secondImage.setImageBitmap(photo);
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, "can't get secondEntry photo");
            }
        });
        secondImage.invalidate();
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

        Log.e(TAG, "Vote info: " + State.chosenBattle.getId() + " " + State.currentUser.getId() + " " +
                winnerId + " " + looserId);

        EntryManager.voteAndDo(State.chosenBattle.getId(), State.currentUser.getId(), winnerId, looserId, new Callback<Vote>() {
            @Override
            public void onResponse(Call<Vote> call, Response<Vote> response) {
                Toast.makeText(VoteActivity.this, "Nice vote, " + State.currentUser.getUsername() + "!", Toast.LENGTH_SHORT).show();
                onBackPressed();
                Log.d(TAG, "vote sent");
            }

            @Override
            public void onFailure(Call<Vote> call, Throwable t) {
                //TODO
                Log.e(TAG, "failed to send vote");
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent voting = new Intent(this, BattleActivity.class);
        startActivity(voting);
    }
}
