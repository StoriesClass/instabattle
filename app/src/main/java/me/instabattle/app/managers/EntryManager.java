package me.instabattle.app.managers;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import me.instabattle.app.models.Entry;
import me.instabattle.app.models.Vote;
import me.instabattle.app.services.LocationService;
import me.instabattle.app.settings.State;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class EntryManager {
    private static final EntryService service = ServiceGenerator.createService(EntryService.class);
    private static EntryService tokenService;

    public static void initTokenService() {
        tokenService = ServiceGenerator.createService(EntryService.class, State.token);
    }

    public static void getByBattleAndDo(int battleId, Callback<List<Entry>> callback) {
        Call<List<Entry>> call = service.getByBattle(battleId);
        call.enqueue(callback);
    }

    public static void getTopByBattleAndDo(int battleId, int count, Callback<List<Entry>> callback) {
        Call<List<Entry>> call = service.getTopByBattle(battleId, count);
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
        LatLng loc = LocationService.getCurrentLocation();
        Call<Entry> call = tokenService.create(new Entry(battleId, userId, loc.latitude, loc.longitude));
        call.enqueue(callback);
    }

    public static void getVoteAndDo(Integer battleId, Callback<List<Entry>> callback) {
        Call<List<Entry>> call = service.getVote(battleId, State.currentUser.getId());
        call.enqueue(callback);
    }

    public static void voteAndDo(Integer battleId, Integer voterId, Integer winnerId, Integer loserId,
                                 Callback<Vote> callback) {
        Call<Vote> call = tokenService.vote(battleId, new Vote(voterId, winnerId, loserId));
        call.enqueue(callback);
    }

    interface EntryService {
        @GET("entries/{entry_id}")
        Call<Entry> get(@Path("entry_id") Integer entryId);

        @GET("users/{user_id}/entries")
        Call<List<Entry>> getByUser(@Path("user_id") Integer userId);

        @GET("battles/{battle_id}/entries")
        Call<List<Entry>> getByBattle(@Path("battle_id") Integer battleId);

        @GET("battles/{battle_id}/entries")
        Call<List<Entry>> getTopByBattle(@Path("battle_id") Integer battleId,
                                         @Query("count") Integer count);

        @GET("battles/{battle_id}/voting")
        Call<List<Entry>> getVote(@Path("battle_id") Integer battleId,
                                  @Query("user_id") Integer userId);

        @POST("battles/{battle_id}/voting")
        Call<Vote> vote(@Path("battle_id") Integer battleId,
                               @Body Vote vote);

        @POST("entries/")
        Call<Entry> create(@Body Entry entry);
    }
}
