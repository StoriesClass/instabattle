package me.instabattle.app.managers;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import me.instabattle.app.BattleCallback;
import me.instabattle.app.models.Battle;
import me.instabattle.app.models.Entry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class BattleManager {
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://instabattle2.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private static final BattleService service =  retrofit.create(BattleService.class);

    public static List<Battle> getNearBattles(LatLng location, double radius) {
        //TODO: send http request to get json with near battles
        //TODO: make battles from json
        return null;
    }

    public static void getAndDo(Integer battleId, final BattleCallback callback) {
        Call<Battle> call = service.getBattle(battleId);

        call.enqueue(new Callback<Battle>() {
            @Override
            public void onResponse(Call<Battle> call, Response<Battle> response) {
                if (response.isSuccessful()) {
                    callback.success(response.body());
                } else {
                    callback.failure();
                }
            }

            @Override
            public void onFailure(Call<Battle> call, Throwable t) {
                callback.failure();
            }
        });
    }

    interface BattleService {
        @GET("battles/")
        Call<List<Battle>> listBattles();

        @GET("battles/{battle_id}")
        Call<Battle> getBattle(@Path("battle_id") Integer battleId);

        @GET("battles/{battle_id}/entries")
        Call<List<Entry>> battleEntries(@Path("battle_id") Integer battleId);

        // FIXME
        //@GET("battles/{battle_id}/voting")
        //Call<List<Entry>> battleEntries(@Path("battle_id") Integer battleId);
    }
}
