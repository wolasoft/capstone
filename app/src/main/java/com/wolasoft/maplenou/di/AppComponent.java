package com.wolasoft.maplenou.di;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ContextModule.class, DataModule.class})
public interface AppComponent {

}
