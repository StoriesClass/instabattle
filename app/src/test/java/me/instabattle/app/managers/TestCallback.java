package me.instabattle.app.managers;

import me.instabattle.app.models.User;
import retrofit2.Call;
import retrofit2.Callback;

import static org.junit.Assert.fail;

public abstract class TestCallback<T> implements Callback<T> {
    @Override
    public void onFailure(Call<T> call, Throwable t) {
        fail(t.getMessage());
    }
}
