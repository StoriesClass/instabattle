package me.instabattle.app.managers;

import java.util.List;

import me.instabattle.app.models.Battle;
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

    public static List<Entry> getEntriesByUser(int userId) {
        //TODO: send http request to get json with users entries [firstEntryNum, firstEntryNum + entriesCount)
        //TODO: make entries from json
        return null;
    }

    public static Entry getEntryById(int entryId) {
        //TODO: bla-bla-bla
        return null;
    }

    interface EntryService {
        @GET("battles/{battle_id}/entries")
        Call<List<Entry>> getEntriesByBattle(@Path("battle_id") Integer battleId);
    }
}
