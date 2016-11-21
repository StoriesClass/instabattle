package me.instabattle.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class BattleActivity extends Activity {

    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);

        title = (TextView) findViewById(R.id.battle_title);
        title.setText(MapActivity.chosenBattle.getName());
    }
}
