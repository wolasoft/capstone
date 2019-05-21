package com.wolasoft.maplenou.views.common;

import android.os.Bundle;

import com.wolasoft.maplenou.R;

public class FailureFeedBackFragment extends FeedBackFragment {

    public static FailureFeedBackFragment newInstance(String descriptionRes) {
        FailureFeedBackFragment fragment = new FailureFeedBackFragment();
        Bundle args = new Bundle();
        args.putString(DESCRIPTION_KEY, descriptionRes);
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
        return R.drawable.wrong;
    }

    @Override
    protected int getColor() {
        return R.color.colorFailure;
    }

    @Override
    protected int getTitle() {
        return R.string.common_failure;
    }

    @Override
    protected int getTitleStyle() {
        return R.style.headline_red_bold;
    }

    @Override
    protected String getDescription() {
        return description;
    }
}
