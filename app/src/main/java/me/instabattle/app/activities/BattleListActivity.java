package me.instabattle.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.List;

import me.instabattle.app.managers.BattleManager;
import me.instabattle.app.R;
import me.instabattle.app.adapters.BattleListAdapter;
import me.instabattle.app.models.Battle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BattleListActivity extends Activity {

    private static final String TAG = "BattleListActivity";

    private ListView battleList;
    private BattleListAdapter battleListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle_list);

        battleList = (ListView) findViewById(R.id.battleList);

        BattleManager.getAllBattlesAndDo(new Callback<List<Battle>>() {
            @Override
            public void onResponse(Call<List<Battle>> call, Response<List<Battle>> response) {
                battleListAdapter = new BattleListAdapter(BattleListActivity.this, response.body());
                battleList.setAdapter(battleListAdapter);
            }

            @Override
            public void onFailure(Call<List<Battle>> call, Throwable t) {
                //TODO
                Log.e(TAG, "cant get battles:" + t);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent voting = new Intent(this, MenuActivity.class);
        startActivity(voting);
    }
}
