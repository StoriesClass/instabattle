package me.instabattle.app.managers;

import java.util.List;

import me.instabattle.app.models.Entry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class EntryManager {
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://instabattle2.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private static final EntryService service = retrofit.create(EntryService.class);

    public static void getEntriesByBattleAndDo(int battleId, Callback<List<Entry>> callback) {
        Call<List<Entry>> call = service.getEntriesByBattle(battleId);
        call.enqueue(callback);
    }

    public static void getEntriesByUserAndDo(int userId, Callback<List<Entry>> callback) {
        Call<List<Entry>> call = service.getEntriesByUser(userId);
        call.enqueue(callback);
    }

    public static void getEntryByIdAndDo(int entryId, Callback<Entry> callback) {
        Call<Entry> call = service.getEntryById(entryId);
        call.enqueue(callback);
    }

    interface EntryService {
        @GET("battles/{battle_id}/entries")
        Call<List<Entry>> getEntriesByBattle(@Path("battle_id") Integer battleId);

        @GET("users/{user_id}/entries")
        Call<List<Entry>> getEntriesByUser(@Path("user_id") Integer userId);

        @GET("entries/{entry_id}")
        Call<Entry> getEntryById(@Path("entry_id") Integer entryId);
    }
}
