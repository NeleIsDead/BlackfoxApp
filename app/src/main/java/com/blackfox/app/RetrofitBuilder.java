package com.blackfox.app;

import android.app.Activity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder extends Activity {
    static String serverAddress = "https://thereawheel3.pythonanywhere.com/";

    public static API api(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serverAddress)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(API.class);
    }
}
