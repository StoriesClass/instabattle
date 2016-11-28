package me.instabattle.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import me.instabattle.app.R;
import me.instabattle.app.State;
import me.instabattle.app.adapters.UserEntryListAdapter;

public class MyProfileActivity extends Activity {

    private TextView userName;
    private ListView userEntryList;
    private UserEntryListAdapter userEntryListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        userName = (TextView) findViewById(R.id.currentUserName);
        userName.setText(State.currentUser.getNickname());

        userEntryListAdapter = new UserEntryListAdapter(this, State.currentUser.getEntries());

        userEntryList = (ListView) findViewById(R.id.userEntryList);
        userEntryList.setAdapter(userEntryListAdapter);
    }

    public void logout(View view) {
        Intent login = new Intent(this, LoginActivity.class);
        startActivity(login);
    }

    @Override
    public void onBackPressed() {
        Intent voting = new Intent(this, MenuActivity.class);
        startActivity(voting);
    }
}
