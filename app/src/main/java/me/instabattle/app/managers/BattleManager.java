package me.instabattle.app.managers;

import android.location.Location;

import java.util.List;

import me.instabattle.app.models.Battle;
import me.instabattle.app.models.Entry;
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

public class BattleManager {
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://instabattle2.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private static final BattleService service = retrofit.create(BattleService.class);

    public static void getAllBattlesAndDo(Callback<List<Battle>> callback) {
        Call<List<Battle>> call = service.getAllBattles();
        call.enqueue(callback);
    }

    public static void getBattleByIdAndDo(Integer battleId, Callback<Battle> callback) {
        Call<Battle> call = service.getBattleById(battleId);
        call.enqueue(callback);
    }

    public static void getInRadiusAndDo(Callback<List<Battle>> callback,
                                        Location location, Double radius) {
        Call<List<Battle>> call = service.getInRadius(location.getLatitude(),
                location.getLongitude(), radius);

        call.enqueue(callback);
    }

    public static void createAndDo(Callback<Battle> callback, Battle battle) {
        Call<Battle> call = service.create(battle);
        call.enqueue(callback);
    }

    public static void getAndDo(Callback<Battle> callback, Integer battleId) {
        Call<Battle> call = service.get(battleId);
        call.enqueue(callback);
    }

    public static void updateAndDo(Callback<Battle> callback, Integer battleId,
                              String name, String description) {
        Call<Battle> call = service.update(battleId, name, description);
        call.enqueue(callback);
    }

    public static void getEntriesAndDo(Callback<List<Entry>> callback, Integer battleId) {
        Call<List<Entry>> call =service.getEntries(battleId);
        call.enqueue(callback);
    }

    public static void getVotingAndDo(Callback<List<Entry>> callback, Integer battleId) {
        // FIXME using List for always two objects
        Call<List<Entry>> call =service.getVoting(battleId);
        call.enqueue(callback);
    }

    interface BattleService {
        @GET("battles/{battle_id}")
        Call<Battle> get(@Path("battle_id") Integer battleId);

        @GET("battles/")
        Call<List<Battle>> getAllBattles();

        @GET("battles/")
        Call<List<Battle>> getInRadius(@Query("latitude") Double lat,
                                            @Query("longitude") Double lon,
                                            @Query("radius") Double radius);

        @GET("battles/{battle_id}")
        Call<Battle> getBattleById(@Path("battle_id") Integer battleId);

        @GET("battles/{battle_id}/entries")
        Call<List<Entry>> getEntries(@Path("battle_id") Integer battleId);

        @GET("battles/{battle_id}/voting")
        Call<List<Entry>> getVoting(@Path("battle_id") Integer battleId);

        @PUT("battles/{battle_id}")
        Call<Battle> update(@Path("battle_id") Integer battleId,
                            @Body String name, @Body String description);

        @POST("battles/")
        Call<Battle> create(@Body Battle battle);
    }
}
