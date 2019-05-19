package com.wolasoft.maplenou.views.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.RemoteViews;

import com.wolasoft.maplenou.MaplenouApplication;
import com.wolasoft.maplenou.R;
import com.wolasoft.maplenou.data.entities.Announcement;
import com.wolasoft.maplenou.data.repositories.AnnouncementRepository;
import com.wolasoft.maplenou.utils.Constants;
import com.wolasoft.waul.utils.CurrencyUtils;
import com.wolasoft.waul.utils.DateUtilities;
import com.wolasoft.waul.utils.ExecutorUtils;
import com.wolasoft.waul.utils.ImageUtils;

import java.io.File;

import javax.inject.Inject;

public class AppWidget extends AppWidgetProvider {

    @Inject
    public AnnouncementRepository repository;
    @Inject
    public ExecutorUtils executorUtils;

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        MaplenouApplication.app().getAppComponent().inject(this);

        this.executorUtils.diskIO().execute(() -> {
            Announcement announcement = repository.getLatest();

            if (announcement != null) {
                File file = ImageUtils.loadImageFromDisk(
                        context, Constants.LOCAL_IMAGE_DIR, announcement.getPhotos().get(0).getFile());
                Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                views.setImageViewBitmap(R.id.thumbnailIMG, bitmap);
                views.setTextViewText(R.id.titleTV, announcement.getTitle());
                views.setTextViewText(
                        R.id.priceTV, CurrencyUtils.formatToCfa(context, announcement.getPrice()));
                views.setTextViewText(
                        R.id.creationDateTV,
                        DateUtilities.format(announcement.getCreationDate(), context));
            } else {
                views.setTextViewText(
                        R.id.titleTV, context.getResources().getString(R.string.message_empty_list));
            }
            appWidgetManager.updateAppWidget(appWidgetId, views);
        });
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) { }

    @Override
    public void onDisabled(Context context) { }
}

