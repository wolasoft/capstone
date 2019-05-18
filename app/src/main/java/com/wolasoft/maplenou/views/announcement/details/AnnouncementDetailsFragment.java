package com.wolasoft.maplenou.views.announcement.details;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.ads.AdRequest;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.synnapps.carouselview.ImageListener;
import com.wolasoft.maplenou.MaplenouApplication;
import com.wolasoft.maplenou.R;
import com.wolasoft.maplenou.data.entities.Announcement;
import com.wolasoft.maplenou.data.entities.Photo;
import com.wolasoft.maplenou.data.repositories.AnnouncementRepository;
import com.wolasoft.maplenou.databinding.FragmentAnnouncementDetailsBinding;
import com.wolasoft.maplenou.utils.Constants;
import com.wolasoft.maplenou.utils.Tracker;
import com.wolasoft.waul.fragments.SimpleFragment;
import com.wolasoft.waul.utils.DateUtilities;
import com.wolasoft.waul.utils.DeviceUtils;
import com.wolasoft.waul.utils.ExecutorUtils;
import com.wolasoft.waul.utils.ImageUtils;
import com.wolasoft.waul.utils.NetworkUtils;
import com.wolasoft.waul.widgets.WDialogBuilder;

import java.util.List;

import javax.inject.Inject;

public class AnnouncementDetailsFragment extends SimpleFragment {
    private static final String ARG_ANNOUNCEMENT_UUID = "UUID";
    public static final String ARG_ANNOUNCEMENT = "announcement";
    private static final int WRITE_PERMISSION_REQUEST_CODE = 1;

    private FragmentAnnouncementDetailsBinding dataBinding;
    @Inject
    public AnnouncementDetailsViewModelFactory factory;
    @Inject
    public AnnouncementRepository announcementRepository;
    @Inject
    public ExecutorUtils executorUtils;
    @Inject
    public Tracker tracker;
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
        tracker.sendFragmentOpenEvent(Tracker.Values.VALUE_ANNOUNCEMENT_DETAILS_FRAGMENT);
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

                if (isAnnouncementSaved) {
                    tracker.sendEvent(Tracker.Event.EVENT_ANNOUNCEMENT_SAVED, null);
                }

                requestWritePermission();

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Toast toast;
        switch (requestCode) {
            case WRITE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    toast = Toast.makeText(
                            getContext(), R.string.permission_message_granted, Toast.LENGTH_LONG);
                    toast.show();
                    saveAnnouncement();
                } else {
                    toast = Toast.makeText(
                            getContext(), R.string.permission_message_refused, Toast.LENGTH_LONG);
                    toast.show();
                }
                break;
            default:
                return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
            dataBinding.progressBar.setVisibility(View.GONE);
        } else {
            AnnouncementDetailsViewModel viewModel = ViewModelProviders.of(this, factory)
                    .get(AnnouncementDetailsViewModel.class);
            viewModel.init(uuid);
            viewModel.getAnnouncementLiveData().observe(this, announcement -> {
                retrievedAnnouncement = announcement;
                setImageListener(announcement.getPhotos());
                dataBinding.images.setPageCount(announcement.getPhotos().size());
                dataBinding.setAnnouncement(announcement);
                dataBinding.creationDate.setText(DateUtilities.format(announcement.getCreationDate(),
                        getContext()));

                dataBinding.emailTV.setOnClickListener(v -> sendMail());
                dataBinding.emailTV.setVisibility(View.VISIBLE);

                if (DeviceUtils.hasPhoneCapability(getContext())) {
                    dataBinding.smsTV.setOnClickListener(v -> sendSms());
                    dataBinding.phoneTV.setOnClickListener(v -> call());
                    dataBinding.smsTV.setVisibility(View.VISIBLE);
                    dataBinding.phoneTV.setVisibility(View.VISIBLE);
                }

            });

            viewModel.getProgressLiveStatus().observe(this, loadingState -> {
                dataBinding.progressBar.setVisibility(View.GONE);
                switch (loadingState) {
                    case ERROR:
                        dataBinding.contentHolder.setVisibility(View.GONE);
                        dataBinding.networkErrorHolder.setVisibility(View.VISIBLE);
                        dataBinding.progressBar.setVisibility(View.GONE);
                        return;
                    case LOADED:
                        dataBinding.contentHolder.setVisibility(View.VISIBLE);
                        dataBinding.networkErrorHolder.setVisibility(View.GONE);
                        dataBinding.progressBar.setVisibility(View.GONE);
                        return;
                }
            });

            viewModel.getDbAnnouncementLiveData().observe(this, announcement -> {
                isAnnouncementSaved = announcement != null;
                invalidateOptionsMenu();
            });
        }

        AdRequest adRequest = new AdRequest.Builder().build();
        dataBinding.adView.loadAd(adRequest);
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

            String message;

            if (isAnnouncementSaved) {
                message = getString(R.string.announcement_details_message_announcement_unsaved);

                for (Photo photo: retrievedAnnouncement.getPhotos()) {
                    String localImageName = photo.getUuid() + Constants.LOCAL_IMAGE_EXT;
                    ImageUtils.deleteFromDisk(
                            getActivity().getApplicationContext(),
                            Constants.LOCAL_IMAGE_DIR,
                            localImageName);
                }

                announcementRepository.delete(retrievedAnnouncement.getUuid());
                isAnnouncementSaved = false;
            } else {
                List<Photo> localPhotos = retrievedAnnouncement.getPhotos();
                int index = 0;
                message = getString(R.string.announcement_details_message_announcement_saved);

                for (Photo photo: retrievedAnnouncement.getPhotos()) {
                    String localImageName = photo.getUuid() + Constants.LOCAL_IMAGE_EXT;
                    downloadImage(photo.getFile(), localImageName);
                    localPhotos.get(index).setFile(localImageName);
                    index++;
                }

                retrievedAnnouncement.setPhotos(localPhotos);
                announcementRepository.save(retrievedAnnouncement);
                isAnnouncementSaved = true;
            }

            getActivity().runOnUiThread(() -> {
                Toast mToast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
                mToast.show();
            });

            invalidateOptionsMenu();
        });
    }

    private void downloadImage(String url, String localName) {

        this.executorUtils.diskIO().execute(() -> {
            Target target = ImageUtils.download(getActivity().getApplicationContext(),
                    Constants.LOCAL_IMAGE_DIR, localName);
            getActivity().runOnUiThread(
                    () -> Picasso.get().load(url).into(target));
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

    private void requestWritePermission() {
        int permission = ContextCompat.checkSelfPermission(
                getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        boolean shouldExplain = ActivityCompat.shouldShowRequestPermissionRationale(
                getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            if (shouldExplain) {
                WDialogBuilder.create(getContext(), "",
                        getString(R.string.permission_write_storage_explanation_message))
                        .setPositiveButton(R.string.common_understand,
                                (dialog, which) -> {
                                    dialog.dismiss();
                                    ActivityCompat.requestPermissions(
                                            getActivity(),
                                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            WRITE_PERMISSION_REQUEST_CODE);
                                })
                        .create().show();

            } else {
                ActivityCompat.requestPermissions(
                        getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        WRITE_PERMISSION_REQUEST_CODE);
            }
        } else {
            saveAnnouncement();
        }
    }
}
