package me.instabattle.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import me.instabattle.app.R;
import me.instabattle.app.services.LocationService;
import me.instabattle.app.settings.State;
import me.instabattle.app.adapters.EntryListAdapter;
import me.instabattle.app.models.Entry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BattleActivity extends Activity {

    private final static String TAG = "BattleActivity";

    private EntryListAdapter entryListAdapter;
    private ListView entryList;

    public static Class<?> gotHereFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);

        ((TextView) findViewById(R.id.battle_title)).setText(State.chosenBattle.getName());

        boolean canParticipate = true;
        if (!LocationService.hasActualLocation() || LocationService.isTooFarFrom(State.chosenBattle)) {
            canParticipate = false;
        }

        findViewById(R.id.participateBtn).setActivated(canParticipate);

        entryList = (ListView) findViewById(R.id.entryList);

        State.chosenBattle.getEntriesAndDo(new Callback<List<Entry>>() {
            @Override
            public void onResponse(Call<List<Entry>> call, Response<List<Entry>> response) {
                entryListAdapter = new EntryListAdapter(BattleActivity.this, response.body());
                entryList.setAdapter(entryListAdapter);
            }

            @Override
            public void onFailure(Call<List<Entry>> call, Throwable t) {
                //TODO
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent voting = new Intent(this, gotHereFrom);
        startActivity(voting);
    }

    public void vote(View v) {
        Intent voting = new Intent(this, VoteActivity.class);
        startActivity(voting);
    }

    public void participate(View v) {
        if (v.isActivated()) {
            Intent participating = new Intent(this, CameraActivity.class);
            startActivity(participating);
        } else {
            String message;
            if (LocationService.hasActualLocation()) {
                message = "You're too far away, come closer to battle for participating!";
                Log.d(TAG, "radius=" + State.chosenBattle.getRadius() + " dist=" + LocationService.getDistanceFromMeTo(State.chosenBattle.getLocation()));
            } else {
                message = "There're problems with detecting your location. Try again later.";
            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }
}
