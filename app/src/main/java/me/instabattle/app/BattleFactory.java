package me.instabattle.app;

import java.util.Arrays;
import java.util.List;

/**
 * Created by wackloner on 21.11.2016.
 */

public class BattleFactory {
    public static List<Battle> getNearBattles() {
        return Arrays.asList(kazansky, isakievsy);
    }

    private static Battle kazansky = new Battle("Kazansky Cathedral", 59.934031, 30.324311);
    private static Battle isakievsy = new Battle("Isaak's Cathedral", 59.934209, 30.305924);
}
