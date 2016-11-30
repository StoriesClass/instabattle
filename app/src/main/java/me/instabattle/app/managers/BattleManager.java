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
        return examples;
    }

    public static Battle getBattleById(int battleId) {
        //TODO: send http request to get json with battle by id
        //TODO: make battle from json
        if (battleId < examples.size()) {
            return examples.get(battleId);
        } else {
            return null;
        }
    }

    private static List<Battle> examples = Arrays.asList(
            new Battle(0, "Kazansky Cathedral", new LatLng(59.934031, 30.324311), 4),
            new Battle(1, "Gallery", new LatLng(59.927615, 30.360142), 6)
    );
}
