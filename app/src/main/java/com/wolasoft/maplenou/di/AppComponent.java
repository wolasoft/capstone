package com.wolasoft.maplenou.di;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ContextModule.class})
public interface AppComponent {

}
