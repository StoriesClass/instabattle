package me.instabattle.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import me.instabattle.app.BattleFactory;
import me.instabattle.app.Entry;
import me.instabattle.app.R;
import me.instabattle.app.User;

public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //example shouldnt be here TODO: fix
        BattleFactory.kazansky.addEntry(new Entry(new User("Qumeric"), BitmapFactory.decodeResource(getResources(), R.drawable.kazansky1)));
        BattleFactory.kazansky.addEntry(new Entry(new User("glebwin"), BitmapFactory.decodeResource(getResources(), R.drawable.kazansky2)));
        BattleFactory.kazansky.addEntry(new Entry(new User("egor_bb"), BitmapFactory.decodeResource(getResources(), R.drawable.kazansky3)));
        BattleFactory.kazansky.addEntry(new Entry(new User("wackloner"), BitmapFactory.decodeResource(getResources(), R.drawable.kazansky4)));

        BattleFactory.gallery.addEntry(new Entry(new User("yeputons"), BitmapFactory.decodeResource(getResources(), R.drawable.gallery1)));
        BattleFactory.gallery.addEntry(new Entry(new User("egor_bb"), BitmapFactory.decodeResource(getResources(), R.drawable.gallery2)));
        BattleFactory.gallery.addEntry(new Entry(new User("wackloner"), BitmapFactory.decodeResource(getResources(), R.drawable.gallery3)));
        BattleFactory.gallery.addEntry(new Entry(new User("Qumeric"), BitmapFactory.decodeResource(getResources(), R.drawable.gallery4)));
        BattleFactory.gallery.addEntry(new Entry(new User("katyakos"), BitmapFactory.decodeResource(getResources(), R.drawable.gallery5)));
        BattleFactory.gallery.addEntry(new Entry(new User("glebwin"), BitmapFactory.decodeResource(getResources(), R.drawable.gallery6)));
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
