package me.instabattle.app.managers;

import com.google.android.gms.maps.model.LatLng;

import java.util.Arrays;
import java.util.List;

import me.instabattle.app.models.Battle;

public class BattleManager {
    public static List<Battle> getNearBattles(LatLng location, double radius) {
        //TODO: send http request to get json with near battles
        //TODO: make battles from json
        //example for debug
        return Arrays.asList(
                new Battle("Gallery", new LatLng(59.927615, 30.360142), 6),
                new Battle("Kazansky Cathedral", new LatLng(59.934031, 30.324311), 4)
        );
    }

    public static Battle getBattleById(int battleId) {
        //TODO: send http request to get json with battle by id
        //TODO: make battle from json
        return null;
    }
}
