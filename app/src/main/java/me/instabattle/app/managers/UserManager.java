package me.instabattle.app.managers;

import java.util.List;

import me.instabattle.app.models.Entry;
import me.instabattle.app.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    public static void getCountAndDo(Callback<List<User>> callback, Integer count) {
        Call<List<User>> call = service.getCount(count);
        call.enqueue(callback);
    }

    public static void getAllAndDo(Callback<List<User>> callback) {
        getCountAndDo(callback, null);
    }

    public static void updateAndDo(Callback<User> callback, Integer userId,
                                   String email, String password) {
        Call<User> call = service.update(userId, email, password);
        call.enqueue(callback);
    }

    public static void getEntriesAndDo(Callback<List<Entry>> callback, Integer userId) {
        Call<List<Entry>> call = service.getEntries(userId);
        call.enqueue(callback);
    }

    public static void createAndDo(Callback<User> callback, User user) {
        Call<User> call = service.create(user);
        call.enqueue(callback);
    }

    interface UserService {
        @GET("users/{user_id}")
        Call<User> getUser(@Path("user_id") Integer userId);

        @GET("users/")
        Call<List<User>> getCount(@Query("count") Integer count);

        @GET("users/{user_id}/entries")
        Call<List<Entry>> getEntries(@Path("user_id") Integer userId);

        @PUT("users/{user_id}")
        Call<User> update(@Path("user_id") Integer userId,
                          @Body String email, @Body String password);

        @POST("users/")
        Call<User> create(@Body User user);
    }
}