package me.instabattle.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import me.instabattle.app.R;
import me.instabattle.app.managers.BattleManager;
import me.instabattle.app.models.Battle;
import me.instabattle.app.models.Entry;
import me.instabattle.app.services.LocationService;
import me.instabattle.app.settings.State;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateBattleActivity extends Activity {

    private static final String TAG = "CreateBattleActivity";

    private TextView newBattleTitle;
    private TextView newBattleDescription;
    private TextView newBattleRadius;
    private ImageView newBattlePhoto;

    private byte[] photoBytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO: save instance
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_battle);

        newBattleTitle = (TextView) findViewById(R.id.newBattleTitle);
        newBattleDescription = (TextView) findViewById(R.id.newBattleDescription);
        newBattleRadius = (TextView) findViewById(R.id.newBattleRadius);
        newBattlePhoto = (ImageView) findViewById(R.id.newBattlePhoto);

        photoBytes = getIntent().getByteArrayExtra("photoBytes");
        if (photoBytes != null) {
            newBattlePhoto.setImageBitmap(BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.length));
        }
    }

    public void sendBattle(View v) {
        Battle newBattle = new Battle(
                newBattleTitle.getText().toString(),
                newBattleDescription.getText().toString(),
                LocationService.getCurrentLocation(),
                Integer.parseInt(newBattleRadius.getText().toString()));
        BattleManager.createAndDo(newBattle, new Callback<Battle>() {
            @Override
            public void onResponse(Call<Battle> call, Response<Battle> response) {
                //TODO
                Log.d(TAG, "new battle was sent");
                addFirstEntry(response.body());
            }

            @Override
            public void onFailure(Call<Battle> call, Throwable t) {
                //TODO
                Log.e(TAG, "can't send new battle: " + t);
            }
        });

        //TODO add focusing on battle on map
        State.creatingBattle = false;
        Intent goToMap = new Intent(this, MapActivity.class);
        startActivity(goToMap);
    }

    private void addFirstEntry(Battle battle) {
        battle.createEntryAndDo(photoBytes, new Callback<Entry>() {
            @Override
            public void onResponse(Call<Entry> call, Response<Entry> response) {
                //TODO
                Log.d(TAG, "new entry was sent");
            }

            @Override
            public void onFailure(Call<Entry> call, Throwable t) {
                //TODO
                Log.e(TAG, "can't send first entry: " + t);
            }
        });
    }

    public void takeBattlePhoto(View v) {
        CameraActivity.gotHereFrom = CreateBattleActivity.class;
        Intent takePhoto = new Intent(this, CameraActivity.class);
        takePhoto.putExtra("battleTitle", newBattleTitle.getText());
        startActivity(takePhoto);
    }
}
