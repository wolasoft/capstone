package com.wolasoft.maplenou.views.favorite.details;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.ads.AdRequest;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.ImageListener;
import com.wolasoft.maplenou.MaplenouApplication;
import com.wolasoft.maplenou.R;
import com.wolasoft.maplenou.data.entities.Announcement;
import com.wolasoft.maplenou.data.entities.Photo;
import com.wolasoft.maplenou.data.repositories.AnnouncementRepository;
import com.wolasoft.maplenou.databinding.FragmentFavoriteDetailsBinding;
import com.wolasoft.maplenou.services.UpdateWidgetService;
import com.wolasoft.maplenou.utils.Constants;
import com.wolasoft.maplenou.utils.Tracker;
import com.wolasoft.waul.fragments.SimpleFragment;
import com.wolasoft.waul.utils.DateUtilities;
import com.wolasoft.waul.utils.DeviceUtils;
import com.wolasoft.waul.utils.ExecutorUtils;
import com.wolasoft.waul.utils.ImageUtils;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

public class FavoriteDetailsFragment extends SimpleFragment {
    private static final String ARG_ANNOUNCEMENT_UUID = "UUID";

    private FragmentFavoriteDetailsBinding dataBinding;
    @Inject
    public FavoriteDetailsViewModelFactory factory;
    @Inject
    public AnnouncementRepository announcementRepository;
    @Inject
    public ExecutorUtils executorUtils;
    @Inject
    public Tracker tracker;
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
        tracker.sendFragmentOpenEvent(Tracker.Values.VALUE_FAVORITE_DETAILS_FRAGMENT);
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
        viewModel.init(uuid);
        viewModel.getAnnouncementLiveData().observe(this, announcement -> {
            dataBinding.progressBar.setVisibility(View.GONE);
            retrievedAnnouncement = announcement;
            if (!isAnnouncementDeleted) {
                setImageListener(announcement.getPhotos());
                dataBinding.images.setPageCount(announcement.getPhotos().size());
                dataBinding.creationDate
                        .setText(DateUtilities.format(announcement.getCreationDate(), getContext()));

                dataBinding.emailTV.setOnClickListener(v -> sendMail());
                dataBinding.emailTV.setVisibility(View.VISIBLE);

                if (DeviceUtils.hasPhoneCapability(getContext())) {
                    dataBinding.smsTV.setOnClickListener(v -> sendSms());
                    dataBinding.phoneTV.setOnClickListener(v -> call());
                    dataBinding.smsTV.setVisibility(View.VISIBLE);
                    dataBinding.phoneTV.setVisibility(View.VISIBLE);
                }
            }
            dataBinding.setAnnouncement(announcement);
        });

        AdRequest adRequest = new AdRequest.Builder().build();
        dataBinding.adView.loadAd(adRequest);
    }

    private void setImageListener(List<Photo> images) {
        ImageListener listener = (position, imageView) -> {
            File imageFile = ImageUtils.loadImageFromDisk(getActivity().getApplicationContext(),
                    Constants.LOCAL_IMAGE_DIR, images.get(position).getFile());
            Picasso.get()
                    .load(imageFile)
                    .error(R.drawable.ic_person_white_24dp)
                    .placeholder(R.drawable.no_image)
                    .into(imageView);
        };
        dataBinding.images.setImageListener(listener);
    }

    private void deleteAnnouncement() {
        this.executorUtils.diskIO().execute(() -> {
            for (Photo photo: retrievedAnnouncement.getPhotos()) {
                String localImageName = photo.getUuid();
                ImageUtils.deleteFromDisk(
                        getActivity().getApplicationContext(),
                        Constants.LOCAL_IMAGE_DIR,
                        localImageName);
            }

            announcementRepository.delete(retrievedAnnouncement);
            isAnnouncementDeleted = true;
            invalidateOptionsMenu();
            updateAppWidget();
        });
    }

    private void call() {
        if (DeviceUtils.hasPhoneCapability(getContext())) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + retrievedAnnouncement.getAccount().getPhoneNumber()));
            Intent chooser = Intent.createChooser(intent, getString(R.string.common_title_make_call));
            if (chooser.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(chooser);
            }
        }
    }

    private void sendSms() {
        if (DeviceUtils.hasPhoneCapability(getContext())) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("smsto:" + retrievedAnnouncement.getAccount().getPhoneNumber()));
            Intent chooser = Intent.createChooser(intent, getString(R.string.common_title_send_sms));
            if (chooser.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(chooser);
            }
        }
    }

    private void sendMail() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("mailto:" + retrievedAnnouncement.getAccount().getEmail()));
        Intent chooser = Intent.createChooser(intent, getString(R.string.common_title_send_email));
        if (chooser.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(chooser);
        }
    }

    private void updateAppWidget() {
        Intent appWidgetService = new Intent(getContext(), UpdateWidgetService.class);
        appWidgetService.setAction(UpdateWidgetService.SHOW_LAST_FAVORITE);
        getActivity().startService(appWidgetService);
    }
}
