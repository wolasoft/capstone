package com.wolasoft.maplenou.services;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.wolasoft.maplenou.views.widgets.AppWidget;

public class UpdateWidgetService extends IntentService {

    public static final String SHOW_LAST_FAVORITE = "show_last_favorite";
    private static final String TAG = "UpdateWidgetService";

    public UpdateWidgetService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (action.equals(SHOW_LAST_FAVORITE)) {
                AppWidgetManager manager = AppWidgetManager.getInstance(getApplicationContext());

                ComponentName componentName =
                        new ComponentName(getApplicationContext(), AppWidget.class);

                int []ids = manager.getAppWidgetIds(componentName);

                Intent widgetIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                getApplicationContext().sendBroadcast(widgetIntent);
            }
        }
    }
}

