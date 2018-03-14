package me.instabattle.app.managers;

import android.graphics.Bitmap;

public interface BitmapCallback {
    void onResponse(Bitmap photo);
    void onFailure(Exception e);
}
