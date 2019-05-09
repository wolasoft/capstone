package com.wolasoft.maplenou.views.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.wolasoft.maplenou.R;
import com.wolasoft.maplenou.databinding.ActivityFeedBackBinding;
import com.wolasoft.waul.activities.BaseActivity;

public class FeedBackActivity extends BaseActivity
        implements FeedBackFragment.OnFeedBackInteractionListener {

    private static Fragment fragment;
    private final String tag = "FEEDBACK_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        replaceFragment(R.id.fragment, fragment, tag, false);
    }

    public static final void setFragment(Fragment f) {
        fragment = f;
    }

    @Override
    public void onButtonClicked() {
        setResult(Activity.RESULT_OK);
        finish();
    }
}
