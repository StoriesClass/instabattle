package me.instabattle.app.managers;

import retrofit2.Call;
import retrofit2.Callback;

import static org.junit.Assert.fail;

abstract class TestCallback<T> implements Callback<T> {
    @Override
    public void onFailure(Call<T> call, Throwable t) {
        fail(t.getMessage());
    }
}
