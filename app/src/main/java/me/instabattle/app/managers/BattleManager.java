package me.instabattle.app.managers;

import android.location.Location;

import java.util.List;

import me.instabattle.app.models.Battle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class BattleManager extends JSONManager {
    private static final BattleService service = retrofit.create(BattleService.class);

    public static void getAllAndDo(Callback<List<Battle>> callback) {
        Call<List<Battle>> call = service.getAll();
        call.enqueue(callback);
    }

    public static void getAndDo(Integer battleId, Callback<Battle> callback) {
        Call<Battle> call = service.get(battleId);
        call.enqueue(callback);
    }

    public static void getInRadiusAndDo(Location location, Double radius,
                                        Callback<List<Battle>> callback) {
        Call<List<Battle>> call = service.getInRadius(location.getLatitude(),
                location.getLongitude(), radius);
        call.enqueue(callback);
    }

    public static void createAndDo(Integer authorId, String name, Double latitude, Double longitude,
                                   String description, Integer radius, Callback<Battle> callback) {
        Call<Battle> call = service.create(new Battle(authorId, name, description, latitude, longitude, radius));
        // FIXME photo actions
        call.enqueue(callback);
    }



    public static void updateAndDo(Integer battleId, String name,
                                   String description, Callback<Battle> callback) {
        Call<Battle> call = service.update(battleId, name, description);
        call.enqueue(callback);
    }

    interface BattleService {
        @GET("battles/{battle_id}")
        Call<Battle> get(@Path("battle_id") Integer battleId);

        @GET("battles/")
        Call<List<Battle>> getAll();

        @GET("battles/")
        Call<List<Battle>> getInRadius(@Query("latitude") Double lat,
                                       @Query("longitude") Double lon,
                                       @Query("radius") Double radius);

        // FIXME
        @PUT("battles/{battle_id}")
        Call<Battle> update(@Path("battle_id") Integer battleId,
                            @Body String name, @Body String description);

        @POST("battles/")
        Call<Battle> create(@Body Battle battle);

    }
}
