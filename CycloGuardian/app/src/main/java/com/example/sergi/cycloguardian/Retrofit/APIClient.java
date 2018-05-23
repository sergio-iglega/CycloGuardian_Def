package com.example.sergi.cycloguardian.Retrofit;

import com.example.sergi.cycloguardian.Utils.Constants;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sergi on 21/05/2018.
 */

public class APIClient {
    private static Retrofit retrofit = null;

    public static Retrofit getRetrofit() {
        OkHttpClient client = new OkHttpClient.Builder().build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }
}
