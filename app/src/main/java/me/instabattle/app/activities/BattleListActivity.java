package me.instabattle.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import me.instabattle.app.BattleFactory;
import me.instabattle.app.R;
import me.instabattle.app.adapters.BattleListAdapter;

public class BattleListActivity extends Activity {

    private ListView battleList;
    private BattleListAdapter battleListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle_list);

        battleListAdapter = new BattleListAdapter(this, BattleFactory.getNearBattles());

        battleList = (ListView) findViewById(R.id.battleList);
        battleList.setAdapter(battleListAdapter);
    }
}
