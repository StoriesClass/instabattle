package me.instabattle.app.managers

import org.junit.Assert
import retrofit2.Call
import retrofit2.Callback

internal abstract class TestCallback<T> : Callback<T> {
    override fun onFailure(call: Call<T>, t: Throwable) {
        Assert.fail(t.message)
    }
}