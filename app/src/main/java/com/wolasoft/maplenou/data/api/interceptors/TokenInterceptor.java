package com.wolasoft.maplenou.data.api.interceptors;

import com.wolasoft.maplenou.data.preferences.AppPreferences;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {
    private AppPreferences preferences;

    @Inject
    public TokenInterceptor(AppPreferences preferences) {
        this.preferences = preferences;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request initialRequest = chain.request();

        if (preferences.getToken() != null) {
            Request request = initialRequest.newBuilder()
                    .header(
                            "Authorization",
                            "Bearer " + preferences.getToken().getAccessToken())
                    .method(initialRequest.method(), initialRequest.body())
                    .build();

            return chain.proceed(request);
        }

        return chain.proceed(initialRequest);
    }
}
