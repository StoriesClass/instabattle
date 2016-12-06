package me.instabattle.app;


import me.instabattle.app.models.Battle;

public interface BattleCallback {
    void success(Battle b);
    void failure();
}
