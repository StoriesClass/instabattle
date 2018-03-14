package me.instabattle.app.managers

import android.util.Base64

import com.google.gson.GsonBuilder

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ServiceGenerator {
    private val API_BASE_URL = "https://instabattle2.herokuapp.com/"
    private val logging = HttpLoggingInterceptor()
    private val httpClient = OkHttpClient.Builder()
    private val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ")
            .create()
    private val builder = Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))

    init {
        // set desired log level
        logging.level = HttpLoggingInterceptor.Level.BODY

        // add logging as last interceptor
        httpClient.interceptors().add(logging)
    }

    fun <S> createService(serviceClass: Class<S>): S {
        return createService(serviceClass, null, null)
    }
    fun <S> createService(serviceClass: Class<S>, email: String? = null, password: String? = null): S {
        if (email != null && password != null) {
            val credentials = "$email:$password"
            val basic = "Basic " + Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)

            httpClient.addInterceptor { chain ->
                val original = chain.request()

                val requestBuilder = original.newBuilder()
                        .header("Authorization", basic)
                        .header("Accept", "application/json")
                        .method(original.method(), original.body())

                val request = requestBuilder.build()
                chain.proceed(request)
            }
        }
        val client = httpClient.build()
        val retrofit = builder.client(client)
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
