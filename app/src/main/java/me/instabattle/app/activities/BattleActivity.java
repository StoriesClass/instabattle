package me.instabattle.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import me.instabattle.app.R;
import me.instabattle.app.services.LocationService;
import me.instabattle.app.settings.State;
import me.instabattle.app.adapters.EntryListAdapter;
import me.instabattle.app.models.Entry;
import me.instabattle.app.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BattleActivity extends Activity {

    private static final String TAG = "BattleActivity";

    private EntryListAdapter entryListAdapter;
    private ListView entryList;

    public static Class<?> gotHereFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);

        ((TextView) findViewById(R.id.battle_title)).setText(State.chosenBattle.getName());
        ((TextView) findViewById(R.id.battle_description)).setText(State.chosenBattle.getDescription());

        entryList = (ListView) findViewById(R.id.entryList);

        State.chosenBattle.getEntriesAndDo(new Callback<List<Entry>>() {
            @Override
            public void onResponse(Call<List<Entry>> call, Response<List<Entry>> response) {
                entryListAdapter = new EntryListAdapter(BattleActivity.this, response.body());
                entryList.setAdapter(entryListAdapter);
            }

            @Override
            public void onFailure(Call<List<Entry>> call, Throwable t) {
                Utils.showToast(BattleActivity.this, "Failed to get battle entries, try again later.");
                Log.e(TAG, "cant get entries: " + t);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent back = new Intent(this, gotHereFrom);
        startActivity(back);
    }

    public void vote(View v) {
        if (State.chosenBattle.getEntriesCount() > 1) {
            State.chosenBattle.getVoteAndDo(new Callback<List<Entry>>() {
                @Override
                public void onResponse(Call<List<Entry>> call, Response<List<Entry>> response) {
                    if (response.code() == 400) {
                        Utils.showToast(BattleActivity.this, "You've already voted for all pairs of photos.");
                        return;
                    }

                    VoteActivity.firstEntry = response.body().get(0);
                    VoteActivity.secondEntry = response.body().get(1);

                    Intent voting = new Intent(BattleActivity.this, VoteActivity.class);
                    startActivity(voting);

                    Log.d(TAG, "got vote");
                }

                @Override
                public void onFailure(Call<List<Entry>> call, Throwable t) {
                    Utils.showToast(BattleActivity.this, "Failed to set new vote, try again later.");
                    Log.e(TAG, "cant get vote: " + t);
                }
            });
        } else {
            Utils.showToast(BattleActivity.this, "Only one entry in battle, you can't vote.");
        }
    }

    public void participate(View v) {
        if (!LocationService.hasActualLocation()) {
            Utils.showToast(BattleActivity.this, "There're problems with detecting your location. Try again later.");
        } else if (LocationService.isTooFarFrom(State.chosenBattle)) {
            Utils.showToast(BattleActivity.this, "You're too far away, come closer to battle for participating!");
        } else {
            CameraActivity.gotHereFrom = BattleActivity.class;
            Intent participating = new Intent(this, CameraActivity.class);
            participating.putExtra("battleTitle", State.chosenBattle.getName());
            startActivity(participating);
        }
    }
}
