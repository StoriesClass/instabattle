package me.instabattle.app

import android.app.Application
import com.chibatching.kotpref.Kotpref
import com.chibatching.kotpref.gsonpref.gson
import com.google.gson.Gson
import com.squareup.leakcanary.LeakCanary

class CustomApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize LeakCanary
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);


        // Initialize Kotpref
        Kotpref.init(this)
        Kotpref.gson = Gson()
    }
}