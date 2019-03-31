package com.wolasoft.maplenou.di;

import com.wolasoft.maplenou.di.modules.ApiModule;
import com.wolasoft.maplenou.di.modules.ContextModule;
import com.wolasoft.maplenou.di.modules.DataModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ApiModule.class, ContextModule.class, DataModule.class})
public interface AppComponent {

}
