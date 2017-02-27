package me.instabattle.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import me.instabattle.app.R;
import me.instabattle.app.images.Util;
import me.instabattle.app.managers.BattleManager;
import me.instabattle.app.managers.PhotoManager;
import me.instabattle.app.models.Battle;
import me.instabattle.app.models.Entry;
import me.instabattle.app.services.LocationService;
import me.instabattle.app.settings.State;
import me.instabattle.app.utils.Utils;
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

    public static byte[] photoBytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_battle);

        newBattleTitle = (TextView) findViewById(R.id.newBattleTitle);
        newBattleDescription = (TextView) findViewById(R.id.newBattleDescription);
        newBattleRadius = (TextView) findViewById(R.id.newBattleRadius);
        newBattlePhoto = (ImageView) findViewById(R.id.newBattlePhoto);

        if (photoBytes != null) {
            newBattlePhoto.setImageBitmap(Util.decodeSampledBitmapFromBytes(photoBytes, 256, 256));
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        newBattleTitle.setText(savedTitle);
        newBattleDescription.setText(savedDescription);
        newBattleRadius.setText(savedRadius);
    }

    private boolean validateBattle() {
        if (getBattleTitle().length() < BATTLE_TITLE_MINIMUM_LENGTH) {
            Utils.showToast(this, "Battle name should have at least " +
                    BATTLE_TITLE_MINIMUM_LENGTH + " characters!");
            return false;
        }
        if (photoBytes == null) {
            Utils.showToast(this, "You should take a first photo in battle!");
            return false;
        }
        return true;
    }

    public void sendBattle(View v) {
        if (!validateBattle()) {
            return;
        }

        final LatLng loc = LocationService.getCurrentLocation();
        BattleManager.createAndDo(State.currentUser.getId(),
                getBattleTitle(),
                loc.latitude,
                loc.longitude,
                getBattleDescription(),
                Integer.parseInt(getBattleRadius()),
                new Callback<Battle>() {
                    @Override
                    public void onResponse(Call<Battle> call, Response<Battle> response) {
                        Log.d(TAG, "new battle was sent");
                        addFirstEntry(response.body());
                    }

                    @Override
                    public void onFailure(Call<Battle> call, Throwable t) {
                        Utils.showToast(CreateBattleActivity.this, "Failed to send battle to server, try again later.");
                        Log.e(TAG, "can't send new battle: " + t);
                    }
        });

        State.creatingBattle = false;
        Intent goToMap = new Intent(this, MapActivity.class);

        clearFields();
        startActivity(goToMap);
    }

    private void addFirstEntry(final Battle battle) {
        battle.createEntryAndDo(new Callback<Entry>() {
            @Override
            public void onResponse(Call<Entry> call, Response<Entry> response) {
                PhotoManager.upload(response.body().getImageName(), photoBytes);
                Log.d(TAG, "new entry was sent");
            }

            @Override
            public void onFailure(Call<Entry> call, Throwable t) {
                addFirstEntry(battle);
                Log.e(TAG, "can't send first entry: " + t);
            }
        });
    }

    public void takeBattlePhoto(View v) {
        ParticipatingActivity.gotHereFrom = CreateBattleActivity.class;
        Intent takePhoto = new Intent(this, ParticipatingActivity.class);

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
