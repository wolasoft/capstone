package com.wolasoft.maplenou;

import android.app.Application;

import com.wolasoft.maplenou.di.AppComponent;
import com.wolasoft.maplenou.di.modules.ContextModule;
import com.wolasoft.maplenou.di.DaggerAppComponent;
import com.wolasoft.maplenou.di.modules.DatabaseModule;

public class MaplenouApplication extends Application {
    private AppComponent appComponent;
    private static MaplenouApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        appComponent = DaggerAppComponent.builder()
                .contextModule(new ContextModule(getApplicationContext()))
                .databaseModule(new DatabaseModule(getApplicationContext()))
                .build();
    }

    public static MaplenouApplication app() {
        return application;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
