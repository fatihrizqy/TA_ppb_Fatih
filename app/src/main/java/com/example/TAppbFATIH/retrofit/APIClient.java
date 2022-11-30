package com.example.TAppbFATIH.retrofit;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    public static final String BASE_URL = "http://commiserable-sunset.000webhostapp.com/";
    public static final String BASE_URL_DWNLD = "";
    private static Retrofit retrofit = null;


    private static Context context;

    public static Retrofit getClient(){

        if (retrofit == null){

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

}
