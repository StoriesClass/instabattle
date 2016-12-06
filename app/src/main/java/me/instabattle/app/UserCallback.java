package me.instabattle.app;


import me.instabattle.app.models.User;

public interface UserCallback {
    void success(User u);
    void failure();
}
