package me.instabattle.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void onButtonClick(View view) {
        Class<?> cls = null;
        switch(view.getId()) {
            case R.id.mapBtn:
                cls = MapActivity.class;
                break;
            case R.id.battlesBtn:
                cls = BattlesActivity.class;
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
}
