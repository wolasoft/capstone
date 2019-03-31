package com.wolasoft.maplenou.di;

import android.content.Context;

import com.google.gson.Gson;
import com.wolasoft.maplenou.data.preferences.AppPreferences;

import dagger.Module;
import dagger.Provides;

@Module
public class DataModule {
    @Provides
    Gson provideGson() {
        return new Gson();
    }

    @Provides
    AppPreferences provideAppPreferences(Context context, Gson gson) {
        return new AppPreferences(context, gson);
    }
}
