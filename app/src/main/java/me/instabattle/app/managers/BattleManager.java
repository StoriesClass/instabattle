package me.instabattle.app.managers;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import me.instabattle.app.models.Battle;
import me.instabattle.app.models.Entry;
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

    public static List<Battle> getNearBattles(LatLng location, double radius) {
        //TODO: send http request to get json with near battles
        //TODO: make battles from json
        return null;
    }

    public static void getAllBattlesAndDo(Callback<List<Battle>> callback) {
        Call<List<Battle>> call = service.listBattles();
        call.enqueue(callback);
    }

    public static void getAndDo(Integer battleId, Callback<Battle> callback) {
        Call<Battle> call = service.getBattle(battleId);
        call.enqueue(callback);
    }

    interface BattleService {
        @GET("battles/")
        Call<List<Battle>> listBattles();

        @GET("battles/{battle_id}")
        Call<Battle> getBattle(@Path("battle_id") Integer battleId);
    }
}
