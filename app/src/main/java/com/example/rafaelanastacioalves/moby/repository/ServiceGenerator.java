package com.example.rafaelanastacioalves.moby.repository;

import com.example.rafaelanastacioalves.moby.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static final AppInterceptor appInterceptor = new AppInterceptor();


    public static <S> S buildService(Class<S> serviceClass) {

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(BuildConfig.API_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());

        return createService(serviceClass, builder);
    }

    public static <S> S buildReactiveService(Class<S> serviceClass) {

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(BuildConfig.API_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

       return createService(serviceClass,builder);
    }

    private static <S> S createService(Class<S> serviceClass, Retrofit.Builder builder) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        Retrofit retrofit = builder.client(httpClient
                .addInterceptor(interceptor)
                .build()).build();
        return retrofit.create(serviceClass);
    }

}
