package com.wolasoft.maplenou.views.announcement.create;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.wolasoft.maplenou.R;
import com.wolasoft.maplenou.data.entities.Category;
import com.wolasoft.maplenou.data.entities.City;
import com.wolasoft.maplenou.databinding.FragmentCreateAnnouncementBinding;
import com.wolasoft.maplenou.views.category.CategoryListActivity;
import com.wolasoft.maplenou.views.city.CityListActivity;
import com.wolasoft.waul.fragments.SimpleFragment;

public class CreateAnnouncementFragment extends SimpleFragment {

    private static final int CATEGORY_LIST_REQUEST_CODE = 1;
    private static final int CITY_LIST_REQUEST_CODE = 2;
    private FragmentCreateAnnouncementBinding dataBinding;

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
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_announcement, container, false);
        replaceToolbar(dataBinding.toolbarHolder.toolbar);
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CATEGORY_LIST_REQUEST_CODE:
                    Category category = data.getParcelableExtra(CategoryListActivity.CATEGORY_KEY);
                    dataBinding.categoryTV.setText(category.getName());
                    break;
                case CITY_LIST_REQUEST_CODE:
                    City city = data.getParcelableExtra(CityListActivity.CITY_KEY);
                    dataBinding.cityTV.setText(city.getName());
                    break;

            }
        }
    }
}
