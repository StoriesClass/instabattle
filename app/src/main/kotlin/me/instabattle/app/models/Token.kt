package me.instabattle.app.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Token {
    @SerializedName("token")
    @Expose
    private val token: String? = null

    fun get(): String? {
        return token
    }
}
