package com.wolasoft.maplenou.utils;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.wolasoft.maplenou.R;
import com.wolasoft.maplenou.data.entities.Photo;

import java.util.List;

import androidx.databinding.BindingAdapter;

public class ImageBindingAdapter {

    @BindingAdapter({"app:imageUrl"})
    public static void loadImage(ImageView view, List<Photo> photos) {
        if (photos != null) {
            String imageUrl = photos.get(0).getFile();

            if (imageUrl != null && !imageUrl.equals("")) {
                Picasso.get()
                        .load(imageUrl)
                        .error(R.drawable.ic_photo_camera_black_24dp)
                        .placeholder(R.drawable.ic_photo_camera_black_24dp)
                        .into(view);
            } else {
                view.setImageDrawable(
                        view.getContext().getResources().getDrawable(R.drawable.ic_photo_camera_black_24dp));
            }
        } else {
            view.setImageResource(R.drawable.ic_photo_camera_black_24dp);
        }
    }
}