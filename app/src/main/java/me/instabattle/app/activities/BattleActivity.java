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
import me.instabattle.app.adapters.ListEntryAdapter;

public class BattleActivity extends Activity {

    private TextView title;
    private ListEntryAdapter listEntryAdapter;
    private ListView entryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);

        title = (TextView) findViewById(R.id.battle_title);
        title.setText(State.chosenBattle.getName());

        listEntryAdapter = new ListEntryAdapter(this, State.chosenBattle.getEntries());

        entryList = (ListView) findViewById(R.id.listEntry);
        entryList.setAdapter(listEntryAdapter);
    }

    @Override
    public void onBackPressed() {
        Intent voting = new Intent(this, State.gotToBattleFrom);
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
