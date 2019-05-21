package com.wolasoft.maplenou.views.fullscreen;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;

import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.ImageListener;
import com.wolasoft.maplenou.R;
import com.wolasoft.maplenou.data.entities.Photo;
import com.wolasoft.maplenou.databinding.ActivityFullScreenBinding;
import com.wolasoft.maplenou.utils.Constants;
import com.wolasoft.waul.activities.BaseActivity;
import com.wolasoft.waul.utils.ImageUtils;

import java.io.File;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

public class FullScreenActivity extends BaseActivity {

    public static final String ARG_PHOTOS = "photos";
    public static final String ARG_IS_LOCAL_PHOTOS = "is_local_photos_sources";
    private ActivityFullScreenBinding dataBinding;
    private boolean isToolbarShown = false;
    private boolean isLocalPhotoSource = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_full_screen);
        setSupportActionBar(dataBinding.appBar.toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_withe_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(R.color.colorTransparent)));

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        List<Photo> photos =
                getIntent().getParcelableArrayListExtra(ARG_PHOTOS);
        isLocalPhotoSource = getIntent().getBooleanExtra(ARG_IS_LOCAL_PHOTOS, false);
        setImageListener(photos);
        dataBinding.imageView.setPageCount(photos.size());

        dataBinding.imageView.setImageClickListener(position -> {
            if (!isToolbarShown) {
                isToolbarShown = true;
                getSupportActionBar().show();
            }
            else {
                isToolbarShown = false;
                getSupportActionBar().hide();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        setResult(Activity.RESULT_CANCELED);
        finish();

        return false;
    }

    private void setImageListener(List<Photo> images) {
        ImageListener listener = (position, imageView) -> {
            if (isLocalPhotoSource) {
                File imageFile = ImageUtils.loadImageFromDisk(getApplicationContext(),
                        Constants.LOCAL_IMAGE_DIR, images.get(position).getFile());
                Picasso.get()
                        .load(imageFile)
                        .error(R.drawable.no_image)
                        .placeholder(R.drawable.no_image)
                        .into(imageView);
            } else {
                Picasso.get()
                        .load(images.get(position).getFile())
                        .error(R.drawable.no_image)
                        .placeholder(R.drawable.no_image)
                        .into(imageView);
            }
            new PhotoViewAttacher(imageView);
        };
        dataBinding.imageView.setImageListener(listener);
    }
}
