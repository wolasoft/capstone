package com.wolasoft.maplenou.data.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wolasoft.maplenou.BuildConfig;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;
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


    private static final HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

    public static <S> S createRetrofitService(Class<S> serviceClass) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.retryOnConnectionFailure(true);
        builder.addInterceptor(logging);

        OkHttpClient client = builder.build();
        Retrofit retrofit = ApiConnector.builder.client(client).build();
        return retrofit.create(serviceClass);
    }
}
