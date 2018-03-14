package me.instabattle.app.managers

import android.graphics.Bitmap

interface BitmapCallback {
    fun onResponse(photo: Bitmap)
    fun onFailure(e: Exception)
}
