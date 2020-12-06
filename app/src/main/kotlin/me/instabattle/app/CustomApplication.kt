package me.instabattle.app

import android.app.Application
import com.chibatching.kotpref.Kotpref
import com.chibatching.kotpref.gsonpref.gson
import com.evernote.android.state.StateSaver
import com.google.gson.Gson

class CustomApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize Kotpref
        Kotpref.init(this)
        Kotpref.gson = Gson()

        // Inititalize Android-State
        StateSaver.setEnabledForAllActivitiesAndSupportFragments(this, true)
    }
}