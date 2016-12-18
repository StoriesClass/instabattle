package me.instabattle.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import me.instabattle.app.R;
import me.instabattle.app.models.Entry;
import me.instabattle.app.settings.State;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotoEditActivity extends Activity {

    private static final String TAG = "PhotoEditActivity";

    private byte[] photoBytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_edit);

        String battleTitle = getIntent().getStringExtra("battleTitle");

        ((TextView) findViewById(R.id.editPhotoBattleTitle)).setText(battleTitle);

        photoBytes = getIntent().getByteArrayExtra("photoBytes");
        Bitmap photoBitmap = BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.length);
        ((ImageView) findViewById(R.id.currentPhoto)).setImageBitmap(photoBitmap);
    }

    public void takeNewPhoto(View v) {
        Intent camera = new Intent(this, CameraActivity.class);
        startActivity(camera);
    }

    public void useThisPhoto(View v) {
        if (!State.creatingBattle) {
            State.chosenBattle.createEntryAndDo(new Callback<Entry>() {
                @Override
                public void onResponse(Call<Entry> call, Response<Entry> response) {
                    // TODO
                }

                @Override
                public void onFailure(Call<Entry> call, Throwable t) {
                    //TODO
                    Log.e(TAG, "failed to add new entry: " + t);
                }
            });
            Intent goToBattle = new Intent(this, BattleActivity.class);
            startActivity(goToBattle);
        } else {
            Intent goToNewBattle = new Intent(this, CreateBattleActivity.class);
            goToNewBattle.putExtra("photoBytes", photoBytes);
            startActivity(goToNewBattle);
        }
    }
}
