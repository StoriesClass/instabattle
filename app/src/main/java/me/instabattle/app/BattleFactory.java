package me.instabattle.app;

import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

import me.instabattle.app.activities.MapActivity;
import me.instabattle.app.activities.RatingActivity;

/**
 * Created by wackloner on 21.11.2016.
 */

public class BattleFactory {
    public static List<Battle> getNearBattles() {
        return Arrays.asList(kazansky, gallery);
    }

    public static Battle gallery = new Battle("Gallery", 59.927615, 30.360142);
    public static Battle kazansky = new Battle("Kazansky Cathedral", 59.934031, 30.324311);
}
