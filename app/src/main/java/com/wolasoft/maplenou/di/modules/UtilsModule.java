package com.wolasoft.maplenou.di.modules;


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
}
