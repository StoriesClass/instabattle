package me.instabattle.app.managers;

import org.junit.Test;

import me.instabattle.app.models.User;
import retrofit2.Call;
import retrofit2.Response;

import static org.junit.Assert.*;

public class UserManagerTest {
    @Test
    public void getAndDo() throws Exception {
        final Integer id = 1;
        UserManager.getAndDo(id, new TestCallback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                assertEquals(1, response.body().getId());
            }
        });
    }

    @Test
    public void getCountAndDo() throws Exception {

    }

    @Test
    public void getAllAndDo() throws Exception {

    }

    @Test
    public void updateAndDo() throws Exception {

    }

    @Test
    public void createAndDo() throws Exception {
        final String username = "ethan";
        final String email = "h3@h3.org";
        final String password = "8383";
        UserManager.createAndDo(username, email, password, new TestCallback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                assertEquals(username, user.getUsername());
                assertEquals(email, user.getEmail());
            }
        });
    }

}