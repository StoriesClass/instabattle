package me.instabattle.app.managers;

import java.util.List;

import me.instabattle.app.models.Token;
import me.instabattle.app.models.User;
import me.instabattle.app.settings.State;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class UserManager {
    private static final String TAG = "UserManager";
    private static final UserService service = ServiceGenerator.createService(UserService.class);
    private static UserService tokenService;

    static void initTokenService() {
        tokenService = ServiceGenerator.createService(UserService.class, State.token);
    }

    public static void getAndDo(Integer userId, Callback<User> callback) {
        service.get(userId).enqueue(callback);
    }

    public static void getAndDo(String username, Callback<User> callback) {
        service.get(username).enqueue(callback);
    }

    private static void getCountAndDo(Integer count, Callback<List<User>> callback) {
        service.getCount(count).enqueue(callback);
    }

    public static void getAllAndDo(Callback<List<User>> callback) {
        getCountAndDo(null, callback);
    }

    public static void updateAndDo(Integer userId, User user, Callback<User> callback) {
        tokenService.update(userId, user).enqueue(callback);
    }

    public static void createAndDo(String username, String email, String password, Callback<User> callback) {
        service.create(new User(username, email, password)).enqueue(callback);
    }

    public static void getTokenAndDo(String email, String password, Callback<Token> callback) {
        UserService loginService = ServiceGenerator.createService(UserService.class,
                email, password);
        loginService.getToken().enqueue(callback);
    }

    interface UserService {
        @GET("users/{user_id}")
        Call<User> get(@Path("user_id") Integer userId);

        @GET("users/{username}")
        Call<User> get(@Path("username") String username);

        @GET("users/")
        Call<List<User>> getCount(@Query("count") Integer count);

        // FIXME
        @PUT("users/{user_id}")
        Call<User> update(@Path("user_id") Integer userId, @Body User user);

        @POST("users/")
        Call<User> create(@Body User user);

        @GET("token")
        Call<Token> getToken();
    }
}