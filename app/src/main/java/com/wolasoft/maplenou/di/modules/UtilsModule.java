package com.wolasoft.maplenou.di.modules;


import android.content.Context;

import com.wolasoft.maplenou.utils.Tracker;
import com.wolasoft.waul.utils.ExecutorUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class UtilsModule {
    @Singleton
    @Provides
    ExecutorUtils provideExecutorUtils() {
        return new ExecutorUtils();
    }

    @Singleton
    @Provides
    Tracker provideTracker(Context context) {
        return new Tracker(context);
    }
}
