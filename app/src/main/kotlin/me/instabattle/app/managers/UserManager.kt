package me.instabattle.app.managers

import io.reactivex.Single
import me.instabattle.app.models.Token
import me.instabattle.app.models.User
import me.instabattle.app.settings.KState
import me.instabattle.app.settings.State
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.*

object UserManager {
    private val TAG = "UserManager"
    private val service = ServiceGenerator.createService(UserService::class.java)
    private var tokenService: UserService? = null

    fun initTokenService() {
        tokenService = ServiceGenerator.createService(UserService::class.java, KState.token)
    }

    fun getAndDo(userId: Int?, callback: Callback<User>) {
        val call = service.get(userId)
        call.enqueue(callback)
    }

    fun getAndDo(username: String, callback: Callback<User>) {
        val call = service.get(username)
        call.enqueue(callback)
    }

    fun getCountAndDo(count: Int?, callback: Callback<List<User>>) {
        val call = service.getCount(count)
        call.enqueue(callback)
    }

    fun getAllAndDo(callback: Callback<List<User>>) {
        getCountAndDo(null, callback)
    }

    fun updateAndDo(userId: Int?, user: User, callback: Callback<User>) {
        val call = tokenService!!.update(userId, user)
        call.enqueue(callback)
    }

    fun createAndDo(username: String, email: String, password: String, callback: Callback<User>) {
        val call = service.create(User(username, email, password))
        call.enqueue(callback)
    }

    fun getTokenAndDo(email: String, password: String, callback: Callback<Token>) {
        val loginService = ServiceGenerator.createService(UserService::class.java,
                email, password)
        val call = loginService.token
        call.enqueue(callback)
    }

    fun create(username: String, email: String, password: String): Single<User> =
            service.createPromise(User(username, email, password))

    fun getToken(email: String, password: String): Single<Token> =
        ServiceGenerator.createService(UserService::class.java, email, password).promiseToken

    fun get(id: Int): Single<User> = service.getPromise(id)

    fun get(username: String): Single<User> = service.getPromise(username)

    internal interface UserService {
        @get:GET("token")
        val promiseToken: Single<Token>

        @POST("users/")
        fun createPromise(@Body user: User): Single<User>

        @GET("users/{user_id}")
        fun getPromise(@Path("user_id") userId: Int?): Single<User>

        @GET("users/{username}")
        fun getPromise(@Path("username") username: String): Single<User>

        @get:GET("token")
        val token: Call<Token>

        @GET("users/{user_id}")
        fun get(@Path("user_id") userId: Int?): Call<User>

        @GET("users/{username}")
        fun get(@Path("username") username: String): Call<User>

        @GET("users/")
        fun getCount(@Query("count") count: Int?): Call<List<User>>

        // FIXME
        @PUT("users/{user_id}")
        fun update(@Path("user_id") userId: Int?, @Body user: User): Call<User>

        @POST("users/")
        fun create(@Body user: User): Call<User>
    }
}