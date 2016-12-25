package me.instabattle.app.models;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Token {
    static private String TAG = "Token";
    @SerializedName("token")
    @Expose
    private String token;

    public Token() {
        Log.i(TAG, "Token constructor called");
    }

    public String get() {
        return token;
    }
}
