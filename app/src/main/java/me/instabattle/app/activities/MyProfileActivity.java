package me.instabattle.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import me.instabattle.app.R;
import me.instabattle.app.activities.LoginActivity;

public class MyProfileActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
    }

    public void logout(View view) {
        Intent login = new Intent(this, LoginActivity.class);
        startActivity(login);
    }
}
