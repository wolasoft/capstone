package com.wolasoft.maplenou.di;

import com.wolasoft.maplenou.di.modules.ApiModule;
import com.wolasoft.maplenou.di.modules.ContextModule;
import com.wolasoft.maplenou.di.modules.DataModule;
import com.wolasoft.maplenou.di.modules.DatabaseModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ApiModule.class, ContextModule.class, DataModule.class, DatabaseModule.class})
public interface AppComponent {

}
