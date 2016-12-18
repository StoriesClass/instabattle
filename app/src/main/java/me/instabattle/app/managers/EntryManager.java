package me.instabattle.app.managers;

import java.util.List;

import me.instabattle.app.models.Entry;
import me.instabattle.app.models.Vote;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class EntryManager extends JSONManager {
    private static final EntryService service = retrofit.create(EntryService.class);

    public static void getByBattleAndDo(int battleId, Callback<List<Entry>> callback) {
        Call<List<Entry>> call = service.getByBattle(battleId);
        call.enqueue(callback);
    }

    public static void getByUserAndDo(int userId, Callback<List<Entry>> callback) {
        Call<List<Entry>> call = service.getByUser(userId);
        call.enqueue(callback);
    }

    public static void getAndDo(Integer entryId, Callback<Entry> callback) {
        Call<Entry> call = service.get(entryId);
        call.enqueue(callback);
    }

    public static void createAndDo(Integer battleId, Integer userId, Callback<Entry> callback) {
        Call<Entry> call = service.create(new Entry(battleId, userId));
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

    public static void voteAndDo(Integer battleId, Integer voterId, Integer winnerId, Integer loserId,
                                 Callback<List<Entry>> callback) {
        Call<List<Entry>> call = service.vote(battleId, new Vote(voterId, winnerId, loserId));
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

        @POST("battle/{battle_id}/entries")
        Call<List<Entry>> vote(@Path("battle_id") Integer battleId,
                               @Body Vote vote);

        @POST("entries")
        Call<Entry> create(@Body Entry entry);
    }
}
