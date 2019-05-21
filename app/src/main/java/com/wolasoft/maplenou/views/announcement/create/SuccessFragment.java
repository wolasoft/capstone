package com.wolasoft.maplenou.views.announcement.create;

import android.os.Bundle;

import com.wolasoft.maplenou.R;
import com.wolasoft.maplenou.views.common.FeedBackFragment;

public class SuccessFragment extends FeedBackFragment {

    public static SuccessFragment newInstance(int descriptionRes) {
        SuccessFragment fragment = new SuccessFragment();
        Bundle args = new Bundle();
        args.putInt(DESCRIPTION_KEY, descriptionRes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            description = getArguments().getString(DESCRIPTION_KEY);
        }
    }

    @Override
    protected int getIcon() {
        return R.drawable.smile;
    }

    @Override
    protected int getColor() {
        return R.color.colorSuccess;
    }

    @Override
    protected int getTitle() {
        return R.string.common_success;
    }

    @Override
    protected int getTitleStyle() {
        return R.style.headline_main_bold;
    }

    @Override
    protected String getDescription() {
        return description;
    }
}
