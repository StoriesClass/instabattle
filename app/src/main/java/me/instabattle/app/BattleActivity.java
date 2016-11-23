package me.instabattle.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class BattleActivity extends Activity {

    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);

        title = (TextView) findViewById(R.id.battle_title);
        title.setText(State.chosenBattle.getName());
    }

    public void vote(View v) {
        Intent voting = new Intent(this, VotingActivity.class);
        startActivity(voting);
    }

    public void participate(View v) {
        Toast.makeText(this, "Participating lol", Toast.LENGTH_SHORT).show();
    }
}
