package com.example.sergi.cycloguardian.Retrofit;

import android.content.Context;

import com.example.sergi.cycloguardian.Utils.Constants;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.sergi.cycloguardian.Retrofit.SSLConfig.getSSLConfig;

/**
 * Created by sergi on 21/05/2018.
 */

public class APIClient {
    private static Retrofit retrofit = null;

    public static Retrofit getRetrofit(Context context) throws CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .sslSocketFactory(getSSLConfig(context).getSocketFactory())
                .build();


        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }
}
