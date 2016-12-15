package me.instabattle.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import me.instabattle.app.R;
import me.instabattle.app.classes.SquareImageView;
import me.instabattle.app.settings.State;

public class PhotoEditActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_edit);

        ((TextView) findViewById(R.id.editPhotoBattleTitle)).setText(State.chosenBattle.getName());

        byte[] photoBytes = getIntent().getByteArrayExtra("takenPhoto");
        Bitmap photoBitmap = BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.length);
        ((ImageView) findViewById(R.id.currentPhoto)).setImageBitmap(photoBitmap);
    }

    public void takeNewPhoto(View v) {
        Intent camera = new Intent(this, CameraActivity.class);
        startActivity(camera);
    }

    public void useThisPhoto(View v) {
        //TODO adding new entry
        Intent battle = new Intent(this, BattleActivity.class);
        startActivity(battle);
    }
}
