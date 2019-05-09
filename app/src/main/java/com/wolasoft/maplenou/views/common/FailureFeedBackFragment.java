package com.wolasoft.maplenou.views.common;

import android.os.Bundle;

import com.wolasoft.maplenou.R;

public class FailureFeedBackFragment extends FeedBackFragment {

    public static FailureFeedBackFragment newInstance(int decriptionRes) {
        FailureFeedBackFragment fragment = new FailureFeedBackFragment();
        Bundle args = new Bundle();
        args.putInt(DESCRIPTION_KEY, decriptionRes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            descriptionRes = getArguments().getInt(DESCRIPTION_KEY);
        }
    }

    @Override
    protected int getIcon() {
        return R.drawable.smile;
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
    protected int getDescription() {
        return descriptionRes;
    }
}
