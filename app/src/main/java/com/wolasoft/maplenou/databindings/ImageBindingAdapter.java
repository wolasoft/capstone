package com.wolasoft.maplenou.databindings;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.squareup.picasso.Picasso;
import com.wolasoft.maplenou.R;
import com.wolasoft.maplenou.data.entities.Photo;
import com.wolasoft.maplenou.utils.Constants;
import com.wolasoft.waul.utils.ImageUtils;

import java.io.File;
import java.util.List;

public class ImageBindingAdapter {

    @BindingAdapter({"app:imageUrl"})
    public static void loadImage(ImageView view, List<Photo> photos) {
        if (photos != null) {
            String imageUrl = photos.get(0).getFile();

            if (imageUrl != null && !imageUrl.equals("")) {
                Picasso.get()
                        .load(imageUrl)
                        .error(R.drawable.no_image)
                        .placeholder(R.drawable.no_image)
                        .into(view);
            } else {
                view.setImageDrawable(
                        view.getContext()
                                .getResources().getDrawable(R.drawable.no_image));
            }
        } else {
            view.setImageResource(R.drawable.no_image);
        }
    }

    @BindingAdapter({"app:localImageUrl"})
    public static void loadLocalImage(ImageView view, List<Photo> photos) {
        if (photos != null) {
            String imageName = photos.get(0).getFile();

            if (imageName != null && !imageName.equals("")) {
                File file = ImageUtils.loadImageFromDisk(
                        view.getContext(), Constants.LOCAL_IMAGE_DIR, imageName);
                Picasso.get()
                        .load(file)
                        .error(R.drawable.no_image)
                        .placeholder(R.drawable.no_image)
                        .into(view);
            } else {
                view.setImageDrawable(
                        view.getContext()
                                .getResources().getDrawable(R.drawable.no_image));
            }
        } else {
            view.setImageResource(R.drawable.no_image);
        }
    }

    @BindingAdapter({"app:profileImage"})
    public static void loadProfile(ImageView view, String imageUrl) {
        if (imageUrl != null && !imageUrl.equals("")) {
            Picasso.get()
                    .load(imageUrl)
                    .error(R.drawable.ic_person_white_24dp)
                    .placeholder(R.drawable.ic_person_white_24dp)
                    .into(view);
        } else {
            view.setImageDrawable(
                    view.getContext().getResources().getDrawable(R.drawable.ic_person_white_24dp));
        }
    }
}