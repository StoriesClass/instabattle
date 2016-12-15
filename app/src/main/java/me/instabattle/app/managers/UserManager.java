package me.instabattle.app.managers;

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

    interface UserService {
        @GET("users/{user_id}")
        Call<User> getUser(@Path("user_id") Integer userId);
    }
}