package com.wolasoft.maplenou.data.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wolasoft.maplenou.BuildConfig;
import com.wolasoft.maplenou.data.api.interceptors.TokenInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiConnector {

    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .create();

    private static final Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BuildConfig.HOST)
                    .addConverterFactory(GsonConverterFactory.create(gson));

    private static Retrofit retrofit;

    private static final HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

    public static <S> S createAuthenticatedRetrofitService(
            Class<S> serviceClass, TokenInterceptor tokenInterceptor) {
        return createRetrofitService(serviceClass, tokenInterceptor);
    }

    public static <S> S createRetrofitService(Class<S> serviceClass) {
        return createRetrofitService(serviceClass, null);
    }

    private static <S> S createRetrofitService(
            Class<S> serviceClass, TokenInterceptor tokenInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.hostnameVerifier((hostname, session) -> true);

        builder.addInterceptor(chain -> {
            Request initialRequest = chain.request();
            Request request = initialRequest.newBuilder()
                    .header("User-Agent", "Maplenou-Android-Application")
                    .header("Accept", "application/json")
                    .method(initialRequest.method(), initialRequest.body())
                    .build();

            return chain.proceed(request);
        });

        if (tokenInterceptor != null) {
            builder.addInterceptor(tokenInterceptor);
        }

        builder.retryOnConnectionFailure(true);
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(logging);

        OkHttpClient client = builder.build();
        retrofit = ApiConnector.builder.client(client).build();
        return retrofit.create(serviceClass);
    }

    public static Retrofit retrofit() {
        return retrofit;
    }
}
