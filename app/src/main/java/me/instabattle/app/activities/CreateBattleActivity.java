package me.instabattle.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

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

    private static final int BATTLE_TITLE_MINIMUM_LENGTH = 1;

    private TextView newBattleTitle;
    private TextView newBattleDescription;
    private TextView newBattleRadius;
    private ImageView newBattlePhoto;

    private static String savedTitle = "";
    private static String savedDescription = "";
    private static String savedRadius = "";

    private String getBattleTitle() {
        return newBattleTitle.getText().toString();
    }

    private String getBattleDescription() {
        return newBattleDescription.getText().toString();
    }

    private String getBattleRadius() {
        return newBattleRadius.getText().toString();
    }

    private byte[] photoBytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

    @Override
    public void onResume() {
        super.onResume();

        newBattleTitle.setText(savedTitle);
        newBattleDescription.setText(savedDescription);
        newBattleRadius.setText(savedRadius);
    }

    private boolean battleIsCorrect() {
        if (getBattleTitle().length() < BATTLE_TITLE_MINIMUM_LENGTH) {
            Toast.makeText(this, "Battle name should have at least " +
                    BATTLE_TITLE_MINIMUM_LENGTH + " characters!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (photoBytes == null) {
            Toast.makeText(this, "You should take a first photo in battle!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void sendBattle(View v) {
        if (!battleIsCorrect()) {
            return;
        }

        final LatLng loc = LocationService.getCurrentLocation();
        BattleManager.createAndDo(State.currentUser.getId(),
                newBattleTitle.getText().toString(),
                loc.latitude,
                loc.longitude,
                newBattleDescription.getText().toString(),
                Integer.parseInt(newBattleRadius.getText().toString()),
                new Callback<Battle>() {
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

        State.creatingBattle = false;
        Intent goToMap = new Intent(this, MapActivity.class);

        clearFields();
        startActivity(goToMap);
    }

    private void addFirstEntry(Battle battle) {
        battle.createEntryAndDo(new Callback<Entry>() {
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
        takePhoto.putExtra("battleTitle", getBattleTitle());

        saveFields();
        startActivity(takePhoto);
    }

    private void saveFields() {
        savedTitle = getBattleTitle();
        savedDescription = getBattleDescription();
        savedRadius = getBattleRadius();
    }

    private void clearFields() {
        savedTitle = savedDescription = savedRadius = "";
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MapActivity.class);

        clearFields();
        State.creatingBattle = false;
        startActivity(intent);
    }
}
