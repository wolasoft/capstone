package com.wolasoft.maplenou.views.announcement.create;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
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
import com.wolasoft.maplenou.views.category.CategoryListActivity;
import com.wolasoft.maplenou.views.city.CityListActivity;
import com.wolasoft.maplenou.views.common.ErrorFeedBackActivity;
import com.wolasoft.maplenou.views.common.FeedBackActivity;
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
    private FragmentCreateAnnouncementBinding dataBinding;
    @Inject
    public AnnouncementRepository repository;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_create_announcement, container, false);
        MaplenouApplication.app().getAppComponent().inject(this);
        setTitle(R.string.announcement_announcement_creation_title);

        initViews();

        return dataBinding.getRoot();
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
                            (dialog, which) -> requestCamera())
                    .setNegativeButton(R.string.message_choose_photo_from_gallery,
                            (dialog, which) -> requestPhotoGallery())
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
                                FeedBackActivity.setFragment(SuccessFragment.newInstance(
                                        R.string.announcement_creation_success_description));
                                Intent intent = new Intent(getContext(), FeedBackActivity.class);
                                dataBinding.progressBar.setVisibility(View.GONE);
                                startActivity(intent);
                            }

                            @Override
                            public void onError(APIError error) {
                                Intent intent = new Intent(getContext(), ErrorFeedBackActivity.class);
                                intent.putExtra(ErrorFeedBackActivity.API_ERROR_KEY, error);
                                dataBinding.progressBar.setVisibility(View.GONE);
                                startActivity(intent);
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

    private void requestPhotoGallery() {
        if (ContextCompat.checkSelfPermission(
                getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                WDialogBuilder.create(getContext(), "",
                        getString(R.string.permission_gallery_explanation_message))
                        .setPositiveButton(R.string.common_understand,
                                (dialog, which) -> {
                            dialog.dismiss();
                                    ActivityCompat.requestPermissions(
                                            getActivity(),
                                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                            GALLERY_PERMISSION_REQUEST_CODE);
                        })
                .create().show();
            } else {
                ActivityCompat.requestPermissions(
                        getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        GALLERY_PERMISSION_REQUEST_CODE);
            }
        } else {
            openImageGallery();
        }
    }

    private void requestCamera() {
        if (ContextCompat.checkSelfPermission(
                getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    getActivity(), Manifest.permission.CAMERA)) {
                WDialogBuilder.create(getContext(), "",
                        getString(R.string.permission_camera_explanation_message))
                        .setPositiveButton(R.string.common_understand,
                                (dialog, which) -> {
                            dialog.dismiss();
                                    ActivityCompat.requestPermissions(
                                            getActivity(), new String[]{Manifest.permission.CAMERA},
                                            CAMERA_PERMISSION_REQUEST_CODE);
                        })
                .create().show();

            } else {
                ActivityCompat.requestPermissions(
                        getActivity(), new String[]{Manifest.permission.CAMERA},
                        CAMERA_PERMISSION_REQUEST_CODE);
            }
        } else {
            openCamera();
        }
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
            case GALLERY_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("GALLERY", "YESSSSS");
                    openImageGallery();
                } else {
                    toast = Toast.makeText(
                            getContext(), R.string.permission_message_refused, Toast.LENGTH_LONG);
                    toast.show();
                }
                return;
            }
            case CAMERA_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("CAMERA", "YESSSSS");
                    openCamera();
                } else {
                    toast = Toast.makeText(
                            getContext(), R.string.permission_message_refused, Toast.LENGTH_LONG);
                    toast.show();
                }
                return;
            }
        }
    }
}
