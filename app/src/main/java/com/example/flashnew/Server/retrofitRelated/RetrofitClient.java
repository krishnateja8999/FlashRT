package com.example.flashnew.Server.retrofitRelated;

import android.content.Context;

import com.example.flashnew.HelperClasses.AppPrefernces;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit = null;


    public static Retrofit getClient(String baseUrl) {

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor("sao.ricardo", "123"))
                .connectTimeout(0, TimeUnit.SECONDS)
                .readTimeout(0, TimeUnit.SECONDS).build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)

                    // .client(new OkHttpClient.Builder().addInterceptor(interceptor).build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
