package com.wolasoft.maplenou.views.favorite.details;


import android.os.Bundle;
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
import com.wolasoft.maplenou.databinding.FragmentFavoriteDetailsBinding;
import com.wolasoft.waul.fragments.SimpleFragment;
import com.wolasoft.waul.utils.DateUtilities;
import com.wolasoft.waul.utils.ExecutorUtils;

import java.util.List;

import javax.inject.Inject;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

public class FavoriteDetailsFragment extends SimpleFragment {
    private static final String ARG_ANNOUNCEMENT_UUID = "UUID";

    private FragmentFavoriteDetailsBinding dataBinding;
    @Inject
    public FavoriteDetailsViewModelFactory factory;
    @Inject
    public AnnouncementRepository announcementRepository;
    @Inject
    public ExecutorUtils executorUtils;
    private MenuItem favoriteMenu;
    private String uuid;
    private Announcement retrievedAnnouncement;
    private boolean isAnnouncementDeleted = false;

    public FavoriteDetailsFragment() { }

    public static FavoriteDetailsFragment newInstance(String uuid) {
        FavoriteDetailsFragment fragment = new FavoriteDetailsFragment();
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
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite_details,
                container, false);

        MaplenouApplication.app().getAppComponent().inject(this);

        initViews();

        return dataBinding.getRoot();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.favorite_announcement_details, menu);
        favoriteMenu = menu.findItem(R.id.action_delete);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                String message =
                        getString(R.string.announcement_details_message_announcement_unsaved);
                Toast mToast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
                mToast.show();
                deleteAnnouncement();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (isAnnouncementDeleted) {
            hideDeletionIcon();
        }

        super.onPrepareOptionsMenu(menu);
    }

    private void hideDeletionIcon() {
        favoriteMenu.setVisible(false);
        dataBinding.deletedItemHolder.setVisibility(View.VISIBLE);
        dataBinding.contentHolder.setVisibility(View.GONE);
    }

    private void initViews() {
        FavoriteDetailsViewModel viewModel = ViewModelProviders
                .of(this, factory)
                .get(FavoriteDetailsViewModel.class);
        // TODO replace 1 with uuid variable
        viewModel.init("1");
        viewModel.getAnnouncementLiveData().observe(this, announcement -> {
            dataBinding.progressBar.setVisibility(View.GONE);
            retrievedAnnouncement = announcement;
            if (!isAnnouncementDeleted) {
                setImageListener(announcement.getPhotos());
                dataBinding.images.setPageCount(announcement.getPhotos().size());
                dataBinding.creationDate
                        .setText(DateUtilities.format(announcement.getCreationDate(), getContext()));
            }
            dataBinding.setAnnouncement(announcement);
        });
    }

    private void setImageListener(List<Photo> images) {
        ImageListener listener = (position, imageView) -> Picasso.get()
                .load(images.get(position).getFile())
                .error(R.drawable.ic_photo_camera_black_24dp)
                .placeholder(R.drawable.ic_photo_camera_black_24dp)
                .into(imageView);
        dataBinding.images.setImageListener(listener);
    }

    private void deleteAnnouncement() {
        this.executorUtils.diskIO().execute(() -> {
            announcementRepository.deleteFromDb(retrievedAnnouncement);
            isAnnouncementDeleted = true;
            invalidateOptionsMenu();
        });
    }
}
