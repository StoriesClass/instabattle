package me.instabattle.app;

import com.google.android.gms.maps.model.LatLng;

import java.util.Arrays;
import java.util.List;

public class BattleManager {
    public static List<Battle> getNearBattles(LatLng location, double radius) {
        //TODO: send http request to get json with near battles
        //TODO: make battles from json
        return Arrays.asList(kazansky, gallery);
    }

    public static Battle getBattleById(int battleId) {
        //TODO: send http request to get json with battle by id
        //TODO: make battle from json
        return null;
    }

    public static Battle gallery = new Battle("Gallery", 59.927615, 30.360142);
    public static Battle kazansky = new Battle("Kazansky Cathedral", 59.934031, 30.324311);
}
