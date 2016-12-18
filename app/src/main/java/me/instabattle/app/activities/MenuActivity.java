package me.instabattle.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import me.instabattle.app.R;
import me.instabattle.app.services.LocationService;

public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        startService(new Intent(this, LocationService.class));
    }

    public void onButtonClick(View view) {
        Class<?> cls = null;
        switch(view.getId()) {
            case R.id.mapBtn:
                cls = MapActivity.class;
                MapActivity.gotHereFrom = MenuActivity.class;
                break;
            case R.id.battlesBtn:
                cls = BattleListActivity.class;
                break;
            case R.id.ratingBtn:
                cls = RatingActivity.class;
                break;
            case R.id.myProfileBtn:
                cls = MyProfileActivity.class;
                break;
        }
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }
}
