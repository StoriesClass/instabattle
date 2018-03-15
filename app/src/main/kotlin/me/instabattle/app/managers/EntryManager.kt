package me.instabattle.app.managers

import me.instabattle.app.models.Entry
import me.instabattle.app.models.Vote
import me.instabattle.app.services.LocationService
import me.instabattle.app.settings.State
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

object EntryManager {
    private val service = ServiceGenerator.createService(EntryService::class.java)
    private var tokenService: EntryService? = null

    fun initTokenService() {
        tokenService = ServiceGenerator.createService(EntryService::class.java, State.token)
    }

    fun getByBattleAndDo(battleId: Int, callback: Callback<List<Entry>>) {
        val call = service.getByBattle(battleId)
        call.enqueue(callback)
    }

    fun getTopByBattleAndDo(battleId: Int, count: Int, callback: Callback<List<Entry>>) {
        val call = service.getTopByBattle(battleId, count)
        call.enqueue(callback)
    }

    fun getByUserAndDo(userId: Int, callback: Callback<List<Entry>>) {
        val call = service.getByUser(userId)
        call.enqueue(callback)
    }

    fun getAndDo(entryId: Int?, callback: Callback<Entry>) {
        val call = service[entryId]
        call.enqueue(callback)
    }

    fun createAndDo(battleId: Int?, userId: Int?, callback: Callback<Entry>) {
        val loc = LocationService.getCurrentLocation()
        val call = tokenService!!.create(Entry(battleId!!, userId!!, loc.latitude, loc.longitude))
        call.enqueue(callback)
    }

    fun getVoteAndDo(battleId: Int?, callback: Callback<List<Entry>>) {
        val call = service.getVote(battleId, State.currentUser.id)
        call.enqueue(callback)
    }

    fun voteAndDo(battleId: Int?, voterId: Int?, winnerId: Int?, loserId: Int?,
                  callback: Callback<Vote>) {
        val call = tokenService!!.vote(battleId, Vote(voterId, winnerId, loserId))
        call.enqueue(callback)
    }

    internal interface EntryService {
        @GET("entries/{entry_id}")
        operator fun get(@Path("entry_id") entryId: Int?): Call<Entry>

        @GET("users/{user_id}/entries")
        fun getByUser(@Path("user_id") userId: Int?): Call<List<Entry>>

        @GET("battles/{battle_id}/entries")
        fun getByBattle(@Path("battle_id") battleId: Int?): Call<List<Entry>>

        @GET("battles/{battle_id}/entries")
        fun getTopByBattle(@Path("battle_id") battleId: Int?,
                           @Query("count") count: Int?): Call<List<Entry>>

        @GET("battles/{battle_id}/voting")
        fun getVote(@Path("battle_id") battleId: Int?,
                    @Query("user_id") userId: Int?): Call<List<Entry>>

        @POST("battles/{battle_id}/voting")
        fun vote(@Path("battle_id") battleId: Int?,
                 @Body vote: Vote): Call<Vote>

        @POST("entries/")
        fun create(@Body entry: Entry): Call<Entry>
    }
}
