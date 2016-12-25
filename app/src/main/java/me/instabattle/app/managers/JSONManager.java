package me.instabattle.app.managers;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

abstract class JSONManager {
    private static final HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    private static final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    public static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ")
            .create();
    public static final Retrofit retrofit;
    static {
        // set desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // add logging as last interceptor
        //httpClient.interceptors().add(logging);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://instabattle2.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();
    }
}
