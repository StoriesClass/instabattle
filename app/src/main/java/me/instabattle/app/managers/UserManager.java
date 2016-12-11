package me.instabattle.app.managers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.instabattle.app.models.Entry;
import me.instabattle.app.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;


public class UserManager {
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://instabattle2.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private static final UserService service = retrofit.create(UserService.class);


    public static void getAndDo(Integer userId, Callback<User> callback) {
        Call<User> call = service.getUser(userId);
        call.enqueue(callback);
    }

    public static List<User> getTopUsers(int count) {
        Call<List<User>> topUsers = service.topUsers();
        try {
            return topUsers.execute().body();
        } catch (IOException e) {
            // FIXME
            return new ArrayList<>();
        }
    }

    interface UserService {
        @GET("users/{user_id}")
        Call<User> getUser(@Path("user_id") Integer userId);

        @GET("users/top")
        Call<List<User>> topUsers();

        @GET("users/")
        Call<List<User>> listUsers();

        @GET("users/{user_id}/entries")
        Call<List<Entry>> userEntries();
    }
}