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

    public static void getByBattleAndDo(int battleId, Callback<List<Entry>> callback) {
        Call<List<Entry>> call = service.getByBattle(battleId);
        call.enqueue(callback);
    }

    public static void getByUserAndDo(int userId, Callback<List<Entry>> callback) {
        Call<List<Entry>> call = service.getByUser(userId);
        call.enqueue(callback);
    }

    public static void getAndDo(int entryId, Callback<Entry> callback) {
        Call<Entry> call = service.get(entryId);
        call.enqueue(callback);
    }

    public static void createAndDo(int battleId, int authorId, String createdOn, byte[] photo, Callback<Entry> callback) {
        //FIXME
        Call<Entry> call = service.get(0);
        call.enqueue(callback);
    }

    public static void getEntriesAndDo(Integer battleId, Callback<List<Entry>> callback) {
        Call<List<Entry>> call =service.getByBattle(battleId);
        call.enqueue(callback);
    }

    public static void getVoteAndDo(Integer battleId, Callback<List<Entry>> callback) {
        Call<List<Entry>> call = service.getVote(battleId);
        call.enqueue(callback);
    }

    public static void voteAndDo(int winnerEntryId, int looserEntryId, Callback<List<Entry>> callback) {
        //FIXME
        Call<List<Entry>> call = service.getByBattle(0);
        call.enqueue(callback);
    }

    interface EntryService {
        @GET("entries/{entry_id}")
        Call<Entry> get(@Path("entry_id") Integer entryId);

        @GET("users/{user_id}/entries")
        Call<List<Entry>> getByUser(@Path("user_id") Integer userId);

        @GET("battles/{battle_id}/entries")
        Call<List<Entry>> getByBattle(@Path("battle_id") Integer battleId);

        @GET("battles/{battle_id}/voting")
        Call<List<Entry>> getVote(@Path("battle_id") Integer battleId);
    }
}
