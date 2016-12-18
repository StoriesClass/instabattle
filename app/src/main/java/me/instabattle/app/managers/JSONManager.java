package me.instabattle.app.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

abstract class JSONManager {
    public static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ")
            .create();
    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://instabattle2.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();
}
