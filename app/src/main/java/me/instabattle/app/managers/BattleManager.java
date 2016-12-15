package me.instabattle.app.managers;

import java.util.List;

import me.instabattle.app.models.Battle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;

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

    interface BattleService {
        @GET("battles/")
        Call<List<Battle>> getAllBattles();

        @GET("battles/{battle_id}")
        Call<Battle> getBattleById(@Path("battle_id") Integer battleId);
    }
}
