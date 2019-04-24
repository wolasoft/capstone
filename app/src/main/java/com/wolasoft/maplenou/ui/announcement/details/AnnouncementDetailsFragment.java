package com.wolasoft.maplenou.ui.announcement.details;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.ImageListener;
import com.wolasoft.maplenou.MaplenouApplication;
import com.wolasoft.maplenou.R;
import com.wolasoft.maplenou.data.entities.Announcement;
import com.wolasoft.maplenou.data.entities.Photo;
import com.wolasoft.maplenou.data.repositories.AnnouncementRepository;
import com.wolasoft.maplenou.databinding.FragmentAnnouncementDetailsBinding;
import com.wolasoft.maplenou.utils.DateUtilities;
import com.wolasoft.maplenou.utils.ExecutorUtils;
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
    @Inject
    public AnnouncementRepository announcementRepository;
    @Inject
    public ExecutorUtils executorUtils;
    private MenuItem favoriteMenu;
    private String uuid;
    private boolean isAnnouncementSaved;
    private Announcement retrievedAnnouncement;

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
        setHasOptionsMenu(true);
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


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.announcement_details, menu);
        favoriteMenu = menu.findItem(R.id.action_favorite);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                String message = isAnnouncementSaved
                        ? getString(R.string.announcement_details_message_announcement_unsaved)
                        : getString(R.string.announcement_details_message_announcement_saved);
                Toast mToast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
                mToast.show();
                saveAnnouncement();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        changeFavoriteIcon();
        super.onPrepareOptionsMenu(menu);
    }

    private void changeFavoriteIcon() {
        if (isAnnouncementSaved) {
            favoriteMenu.setIcon(R.drawable.ic_star_white_24dp);

        } else {
            favoriteMenu.setIcon(R.drawable.ic_star_border_white_24dp);
        }
    }

    private void initViews() {
        if (!NetworkUtils.isInternetAvailable(getContext())) {
            dataBinding.networkErrorHolder.setVisibility(View.VISIBLE);
        } else {
            AnnouncementDetailsViewModel viewModel = ViewModelProviders.of(this, factory)
                    .get(AnnouncementDetailsViewModel.class);
            viewModel.init("1");
            viewModel.getAnnouncementLiveData().observe(this, announcement -> {
                retrievedAnnouncement = announcement;
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
                        dataBinding.contentHolder.setVisibility(View.GONE);
                        return;
                    case LOADED:
                        dataBinding.networkErrorHolder.setVisibility(View.GONE);
                        dataBinding.contentHolder.setVisibility(View.VISIBLE);
                        return;
                }
            });

            viewModel.getDbAnnouncementLiveData().observe(this, announcement -> {
                isAnnouncementSaved = announcement != null;
                Log.d("TAGGGGGGGGGGG", Boolean.toString(isAnnouncementSaved));
                getActivity().invalidateOptionsMenu();
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

    private void saveAnnouncement() {
        this.executorUtils.diskIO().execute(() -> {
            if (retrievedAnnouncement == null) {
                return;
            }

            if (isAnnouncementSaved) {
                announcementRepository.deleteFromDb(retrievedAnnouncement);
                isAnnouncementSaved = false;
            } else {
                announcementRepository.saveToDb(retrievedAnnouncement);
                isAnnouncementSaved = true;
            }
            getActivity().invalidateOptionsMenu();
        });
    }
}
