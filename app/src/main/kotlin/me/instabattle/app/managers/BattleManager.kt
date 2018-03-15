package me.instabattle.app.managers

import android.location.Location

import me.instabattle.app.models.Battle
import me.instabattle.app.settings.State
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

object BattleManager {
    private val service = ServiceGenerator.createService(BattleService::class.java)
    private var tokenService: BattleService? = null

    fun initTokenService() {
        tokenService = ServiceGenerator.createService(BattleService::class.java, State.token)
    }

    fun getAllAndDo(callback: Callback<List<Battle>>) {
        val call = service.all
        call.enqueue(callback)
    }

    fun getAndDo(battleId: Int?, callback: Callback<Battle>) {
        val call = service[battleId]
        call.enqueue(callback)
    }

    fun getInRadiusAndDo(location: Location, radius: Double?,
                         callback: Callback<List<Battle>>) {
        val call = service.getInRadius(location.latitude,
                location.longitude, radius)
        call.enqueue(callback)
    }

    fun createAndDo(authorId: Int?, name: String, latitude: Double?, longitude: Double?,
                    description: String, radius: Int?, callback: Callback<Battle>) {
        val call = tokenService!!.create(Battle(authorId, name, description, latitude, longitude, radius))
        call.enqueue(callback)
    }


    fun updateAndDo(battleId: Int?, name: String,
                    description: String, callback: Callback<Battle>) {
        val call = tokenService!!.update(battleId, name, description)
        call.enqueue(callback)
    }

    internal interface BattleService {

        @get:GET("battles/")
        val all: Call<List<Battle>>

        @GET("battles/{battle_id}")
        operator fun get(@Path("battle_id") battleId: Int?): Call<Battle>

        @GET("battles/")
        fun getInRadius(@Query("latitude") lat: Double?,
                        @Query("longitude") lon: Double?,
                        @Query("radius") radius: Double?): Call<List<Battle>>

        // FIXME
        @PUT("battles/{battle_id}")
        fun update(@Path("battle_id") battleId: Int?,
                   @Body name: String, @Body description: String): Call<Battle>

        @POST("battles/")
        fun create(@Body battle: Battle): Call<Battle>
    }
}
