package me.instabattle.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import me.instabattle.app.R;
import me.instabattle.app.State;
import me.instabattle.app.adapters.EntryListAdapter;

public class BattleActivity extends Activity {

    private TextView title;
    private EntryListAdapter entryListAdapter;
    private ListView entryList;

    public static Class<?> gotHereFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);

        title = (TextView) findViewById(R.id.battle_title);
        title.setText(State.chosenBattle.getName());

        entryListAdapter = new EntryListAdapter(this, State.chosenBattle.getEntries(0, 0));

        entryList = (ListView) findViewById(R.id.entryList);
        entryList.setAdapter(entryListAdapter);
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
        Toast.makeText(this, "Participating lol", Toast.LENGTH_SHORT).show();
    }
}
