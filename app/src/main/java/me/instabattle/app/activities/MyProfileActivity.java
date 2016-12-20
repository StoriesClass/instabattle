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
import me.instabattle.app.settings.State;
import me.instabattle.app.adapters.UserEntryListAdapter;
import me.instabattle.app.models.Entry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProfileActivity extends Activity {

    private static final String TAG = "MyProfileActivity";

    private TextView userName;
    private ListView userEntryList;
    private UserEntryListAdapter userEntryListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        userName = (TextView) findViewById(R.id.currentUserName);
        userName.setText(State.currentUser.getUsername());

        userEntryList = (ListView) findViewById(R.id.userEntryList);

        State.currentUser.getEntriesAndDo(new Callback<List<Entry>>() {
            @Override
            public void onResponse(Call<List<Entry>> call, Response<List<Entry>> response) {
                Log.d(TAG, "got user entries");
                userEntryListAdapter = new UserEntryListAdapter(MyProfileActivity.this, response.body());
                userEntryList.setAdapter(userEntryListAdapter);
            }

            @Override
            public void onFailure(Call<List<Entry>> call, Throwable t) {
                //TODO
                Log.e(TAG, "cant get entries: " + t);
            }
        });
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
