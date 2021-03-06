package com.wolasoft.maplenou.views.announcement.create;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.wolasoft.maplenou.MaplenouApplication;
import com.wolasoft.maplenou.R;
import com.wolasoft.maplenou.data.api.errors.APIError;
import com.wolasoft.maplenou.data.api.services.CallBack;
import com.wolasoft.maplenou.data.entities.Announcement;
import com.wolasoft.maplenou.data.entities.Category;
import com.wolasoft.maplenou.data.entities.City;
import com.wolasoft.maplenou.data.repositories.AnnouncementRepository;
import com.wolasoft.maplenou.databinding.FragmentCreateAnnouncementBinding;
import com.wolasoft.maplenou.utils.Tracker;
import com.wolasoft.maplenou.views.about.AboutActivity;
import com.wolasoft.maplenou.views.category.CategoryListActivity;
import com.wolasoft.maplenou.views.city.CityListActivity;
import com.wolasoft.waul.fragments.SimpleFragment;
import com.wolasoft.waul.utils.DimensionUtils;
import com.wolasoft.waul.utils.ImageUtils;
import com.wolasoft.waul.widgets.ClosableImageView;
import com.wolasoft.waul.widgets.WDialogBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class CreateAnnouncementFragment extends SimpleFragment {

    private static final int CATEGORY_LIST_REQUEST_CODE = 1;
    private static final int CITY_LIST_REQUEST_CODE = 2;
    private static final int PICK_IMAGE_REQUEST_CODE = 3;
    private static final int TAKE_IMAGE_REQUEST_CODE = 4;
    private static final int GALLERY_PERMISSION_REQUEST_CODE = 6;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 7;
    private OnAnnounceCreationInteractionListener mListener;
    private FragmentCreateAnnouncementBinding dataBinding;
    @Inject
    public AnnouncementRepository repository;
    @Inject
    public Tracker tracker;
    private int numberOfImage = 0;
    private List<File> photos;
    private Category category;
    private City city;

    public CreateAnnouncementFragment() { }

    public static CreateAnnouncementFragment newInstance() {
        CreateAnnouncementFragment fragment = new CreateAnnouncementFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_create_announcement, container, false);
        MaplenouApplication.app().getAppComponent().inject(this);
        tracker.sendFragmentOpenEvent(Tracker.Values.VALUE_CREATE_ANOUNCEMENT_FRAGMENT);

        initViews();

        return dataBinding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.message, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                Intent intent = new Intent(getContext(), AboutActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAnnounceCreationInteractionListener) {
            mListener = (OnAnnounceCreationInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAnnounceCreationInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void initViews() {
        dataBinding.categoryTV.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CategoryListActivity.class);
            startActivityForResult(intent, CATEGORY_LIST_REQUEST_CODE);
        });

        dataBinding.cityTV.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CityListActivity.class);
            startActivityForResult(intent, CITY_LIST_REQUEST_CODE);
        });

        dataBinding.selectImage.setOnClickListener(v -> {
            WDialogBuilder.create(getContext(), getString(R.string.message_image_source), "")
                    .setPositiveButton(R.string.message_take_photo,
                            (dialog, which) -> requestCameraPermission())
                    .setNegativeButton(R.string.message_choose_photo_from_gallery,
                            (dialog, which) -> requestPhotoGalleryPermission())
                    .create().show();
        });

        dataBinding.validateButton.setOnClickListener(
                v -> {
                    if (!isFormValid()) {
                        return;
                    }

                    dataBinding.contentHolder.setVisibility(View.GONE);
                    dataBinding.progressBar.setVisibility(View.VISIBLE);

                    this.repository.create(
                        dataBinding.titleEdit.getText().toString(),
                        dataBinding.priceEdit.getText().toString(),
                        dataBinding.descriptionEdit.getText().toString(),
                        dataBinding.localizationEdit.getText().toString(),
                        city.getUuid(),
                        category.getUuid(),
                        photos,
                        new CallBack<Announcement>() {
                            @Override
                            public void onSuccess(Announcement data) {
                                dataBinding.progressBar.setVisibility(View.GONE);
                                Bundle bundle = new Bundle();
                                bundle.putString(Tracker.Params.PARAM_CREATION_STATE, "success");
                                tracker.sendEvent(Tracker.Event.EVENT_ANNOUNCEMENT_CREATION, bundle);

                                if (mListener != null) {
                                    mListener.onAnnounceCreated();
                                }
                            }

                            @Override
                            public void onError(APIError error) {
                                Bundle bundle = new Bundle();
                                bundle.putString(Tracker.Params.PARAM_CREATION_STATE, "failed");
                                tracker.sendEvent(Tracker.Event.EVENT_ANNOUNCEMENT_CREATION, bundle);
                                dataBinding.progressBar.setVisibility(View.GONE);

                                if (mListener != null) {
                                    mListener.onAnnounceCreationFailed(error);
                                }
                            }
                        });
                });
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, TAKE_IMAGE_REQUEST_CODE);
        }
    }

    private void openImageGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        Intent chooserIntent = Intent.createChooser(
                intent, getString(R.string.common_image_picker_title));

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(chooserIntent, PICK_IMAGE_REQUEST_CODE);
        }
    }

    private void addImage(File file) {
        if (photos == null) {
            photos = new ArrayList<>();
        }

        photos.add(file);
    }

    private void removeImage(ClosableImageView imageView) {
        imageView.getCloseButton().setOnClickListener(v -> {
            int childIndex = dataBinding.imagesHolder.indexOfChild(imageView);
            photos.remove(childIndex);
            dataBinding.imagesHolder.removeView(imageView);
            numberOfImage--;
        });
    }

    private void showImage(Uri imageUri) {
        ClosableImageView imageView = new ClosableImageView(getContext());
        imageView.setImage(imageUri);
        setImageSize(imageView);
        dataBinding.imagesHolder.addView(imageView, numberOfImage);
        removeImage(imageView);
        numberOfImage++;
    }

    private void showImage(Bitmap bitmap) {
        ClosableImageView imageView = new ClosableImageView(getContext());
        imageView.setImage(bitmap);
        setImageSize(imageView);
        dataBinding.imagesHolder.addView(imageView, numberOfImage);
        removeImage(imageView);
        numberOfImage++;
    }

    private void setImageSize(View view) {
        view.setLayoutParams(
                new LinearLayout.LayoutParams(
                        DimensionUtils.dpToPx(getContext(),75),
                        DimensionUtils.dpToPx(getContext(),75)));
    }

    private void requestPhotoGalleryPermission() {
        int permission = ContextCompat.checkSelfPermission(
                getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            boolean shouldExplain =
                    shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);

            if (shouldExplain) {
                WDialogBuilder.create(getContext(), "",
                        getString(R.string.permission_gallery_explanation_message))
                        .setPositiveButton(R.string.common_understand,
                                (dialog, which) ->
                                    requestPermissions(
                                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                            GALLERY_PERMISSION_REQUEST_CODE))
                .create().show();

                return;
            }

            requestPermissions(
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    GALLERY_PERMISSION_REQUEST_CODE);

            return;
        }

        openImageGallery();
    }

    private void requestCameraPermission() {
        int permission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            boolean shouldExplain = shouldShowRequestPermissionRationale(Manifest.permission.CAMERA);

            if (shouldExplain) {
                WDialogBuilder.create(getContext(), "",
                        getString(R.string.permission_camera_explanation_message))
                        .setPositiveButton(R.string.common_understand,
                                (dialog, which) -> requestPermissions(
                                            new String[]{Manifest.permission.CAMERA},
                                            CAMERA_PERMISSION_REQUEST_CODE))
                .create().show();

                return;

            }

            requestPermissions(
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);

            return;
        }

        openCamera();
    }

    private boolean isFormValid() {
        if (category == null) {
            WDialogBuilder.create(getContext(), "",
                    getString(R.string.announcement_creation_category_required))
                    .setPositiveButton(R.string.common_understand,
                            (dialog, which) -> dialog.dismiss())
            .create().show();
            return false;
        }

        if (dataBinding.titleEdit.getText().toString().trim().equals("")) {
            dataBinding.titleLayout.setError(getString(R.string.common_required));
            return false;
        }

        if (dataBinding.priceEdit.getText().toString().trim().equals("")) {
            dataBinding.priceLayout.setError(getString(R.string.common_required));
            return false;
        }

        if (dataBinding.descriptionEdit.getText().toString().trim().equals("")) {
            dataBinding.descriptionLayout.setError(getString(R.string.common_required));
            return false;
        }

        if (city == null) {
            WDialogBuilder.create(getContext(), "",
                    getString(R.string.announcement_creation_city_required))
                    .setPositiveButton(R.string.common_understand,
                            (dialog, which) -> dialog.dismiss())
                    .create().show();
            return false;
        }

        if (dataBinding.localizationEdit.getText().toString().trim().equals("")) {
            dataBinding.localizationLayout.setError(getString(R.string.common_required));
            return false;
        }

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CATEGORY_LIST_REQUEST_CODE:
                    category = data.getParcelableExtra(CategoryListActivity.CATEGORY_KEY);
                    dataBinding.categoryTV.setText(category.getName());
                    break;
                case CITY_LIST_REQUEST_CODE:
                    city = data.getParcelableExtra(CityListActivity.CITY_KEY);
                    dataBinding.cityTV.setText(city.getName());
                    break;
                case PICK_IMAGE_REQUEST_CODE:
                    Uri selectedImageUri = data.getData();
                    Bitmap uriBitmap = null;
                    try {
                        uriBitmap = MediaStore.Images.Media.getBitmap(
                                getContext().getContentResolver(), selectedImageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    File uriFile = ImageUtils.createTempFileFromBitmap(getActivity(), uriBitmap);
                    addImage(uriFile);
                    showImage(selectedImageUri);
                    break;
                case TAKE_IMAGE_REQUEST_CODE:
                    Bundle extras = data.getExtras();
                    Bitmap bitmap = (Bitmap) extras.get("data");
                    File bitmapFile = ImageUtils.createTempFileFromBitmap(getActivity(), bitmap);
                    addImage(bitmapFile);
                    showImage(bitmap);
                    break;

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Toast toast;
        switch (requestCode) {
            case GALLERY_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImageGallery();
                } else {
                    toast = Toast.makeText(getContext(), R.string.permission_message_refused, Toast.LENGTH_LONG);
                    toast.show();
                }
                return;
            case CAMERA_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    toast = Toast.makeText(getContext(), R.string.permission_message_refused, Toast.LENGTH_LONG);
                    toast.show();
                }
                return;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public interface OnAnnounceCreationInteractionListener {
        void onAnnounceCreated();
        void onAnnounceCreationFailed(APIError error);
    }
}
