package me.instabattle.app.managers

import android.util.Base64

import com.google.gson.GsonBuilder
import me.instabattle.app.BuildConfig

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ServiceGenerator {
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // set desired log level
    }
    private val httpClient = OkHttpClient.Builder().apply {
        interceptors().add(logging) // add logging as last interceptor
    }
    private val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ")
            .create()
    private val builder = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))

    fun <S> createService(serviceClass: Class<S>): S {
        return createService(serviceClass, null, null)
    }
    fun <S> createService(serviceClass: Class<S>, email: String? = null, password: String? = null): S {
        if (email != null && password != null) {
            val credentials = "$email:$password"
            val basic = "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)

            httpClient.addInterceptor {
                val original = it.request()

                val requestBuilder = original.newBuilder()
                        .header("Authorization", basic)
                        .header("Accept", "application/json")
                        .method(original.method(), original.body())

                val request = requestBuilder.build()
                it.proceed(request)
            }
        }
        val retrofit = builder.client(httpClient.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        return retrofit.create(serviceClass)
    }

    fun <S> createService(serviceClass: Class<S>, authToken: String): S {
        return createService(serviceClass, authToken, "")
    }

    fun initTokenServices() {
        BattleManager.initTokenService()
        EntryManager.initTokenService()
        UserManager.initTokenService()
    }
}
