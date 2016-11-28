package me.instabattle.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import me.instabattle.app.BattleManager;
import me.instabattle.app.Entry;
import me.instabattle.app.R;
import me.instabattle.app.UserFactory;

public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //example shouldnt be here TODO: fix
        if (!init) {
            initExamples();
            init = true;
        }
    }

    private boolean init = false;

    private void initExamples() {
        BattleManager.kazansky.addEntry(new Entry(UserFactory.qumeric, BitmapFactory.decodeResource(getResources(), R.drawable.kazansky1)));
        BattleManager.kazansky.addEntry(new Entry(UserFactory.glebwin, BitmapFactory.decodeResource(getResources(), R.drawable.kazansky2)));
        BattleManager.kazansky.addEntry(new Entry(UserFactory.egor_bb, BitmapFactory.decodeResource(getResources(), R.drawable.kazansky3)));
        BattleManager.kazansky.addEntry(new Entry(UserFactory.wackloner, BitmapFactory.decodeResource(getResources(), R.drawable.kazansky4)));

        BattleManager.gallery.addEntry(new Entry(UserFactory.yeputons, BitmapFactory.decodeResource(getResources(), R.drawable.gallery1)));
        BattleManager.gallery.addEntry(new Entry(UserFactory.egor_bb, BitmapFactory.decodeResource(getResources(), R.drawable.gallery2)));
        BattleManager.gallery.addEntry(new Entry(UserFactory.wackloner, BitmapFactory.decodeResource(getResources(), R.drawable.gallery3)));
        BattleManager.gallery.addEntry(new Entry(UserFactory.qumeric, BitmapFactory.decodeResource(getResources(), R.drawable.gallery4)));
        BattleManager.gallery.addEntry(new Entry(UserFactory.katyakos, BitmapFactory.decodeResource(getResources(), R.drawable.gallery5)));
        BattleManager.gallery.addEntry(new Entry(UserFactory.glebwin, BitmapFactory.decodeResource(getResources(), R.drawable.gallery6)));
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
