package com.wolasoft.maplenou.ui.announcement.details;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.ImageListener;
import com.wolasoft.maplenou.MaplenouApplication;
import com.wolasoft.maplenou.R;
import com.wolasoft.maplenou.data.entities.Photo;
import com.wolasoft.maplenou.databinding.FragmentAnnouncementDetailsBinding;
import com.wolasoft.maplenou.utils.DateUtilities;
import com.wolasoft.maplenou.utils.NetworkUtils;

import java.util.List;

import javax.inject.Inject;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class AnnouncementDetailsFragment extends Fragment {
    private static final String ARG_ANNOUNCEMENT_UUID = "UUID";

    private FragmentAnnouncementDetailsBinding dataBinding;
    @Inject
    public AnnouncementDetailsViewModelFactory factory;
    private String uuid;

    public AnnouncementDetailsFragment() { }

    public static AnnouncementDetailsFragment newInstance(String uuid) {
        AnnouncementDetailsFragment fragment = new AnnouncementDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ANNOUNCEMENT_UUID, uuid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uuid = getArguments().getString(ARG_ANNOUNCEMENT_UUID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_announcement_details,
                container, false);

        MaplenouApplication.app().getAppComponent().inject(this);

        initViews();

        return dataBinding.getRoot();
    }

    private void initViews() {
        if (!NetworkUtils.isInternetAvailable(getContext())) {
            dataBinding.networkErrorHolder.setVisibility(View.VISIBLE);
        } else {
            AnnouncementDetailsViewModel viewModel = ViewModelProviders.of(this, factory)
                    .get(AnnouncementDetailsViewModel.class);
            viewModel.init("1");
            viewModel.getAnnouncementLiveData().observe(this, announcement -> {
                setImageListener(announcement.getPhotos());
                dataBinding.images.setPageCount(announcement.getPhotos().size());
                dataBinding.setAnnouncement(announcement);
                dataBinding.creationDate.setText(DateUtilities.format(announcement.getCreationDate(), getContext()));
            });

            viewModel.getProgressLiveStatus().observe(this, loadingState -> {
                dataBinding.progressBar.setVisibility(View.GONE);
                switch (loadingState) {
                    case ERROR:
                        dataBinding.networkErrorHolder.setVisibility(View.VISIBLE);
                        return;
                    case LOADED:
                        return;
                }
            });
        }
    }

    private void setImageListener(List<Photo> images) {
        ImageListener listener = (position, imageView) -> Picasso.get()
                .load(images.get(position).getFile())
                .error(R.drawable.ic_photo_camera_black_24dp)
                .placeholder(R.drawable.ic_photo_camera_black_24dp)
                .into(imageView);
        dataBinding.images.setImageListener(listener);
    }
}
