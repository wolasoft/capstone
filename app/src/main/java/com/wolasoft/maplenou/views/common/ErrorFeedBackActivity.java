package com.wolasoft.maplenou.views.common;

import android.app.Activity;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.wolasoft.maplenou.R;
import com.wolasoft.maplenou.data.api.errors.APIError;
import com.wolasoft.maplenou.databinding.ActivityFeedBackBinding;
import com.wolasoft.waul.activities.BaseActivity;

public class ErrorFeedBackActivity extends BaseActivity
        implements FeedBackFragment.OnFeedBackInteractionListener {

    public static final String API_ERROR_KEY = "API_ERROR_KEY";
    private ActivityFeedBackBinding dataBinding;
    private static APIError error;
    private final String tag = "FEEDBACK_TAG";
    private String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_feed_back);

        if (getIntent().hasExtra(API_ERROR_KEY)) {
            error = getIntent().getParcelableExtra(API_ERROR_KEY);
            initError();
        } else {
            description = getString(R.string.http_error_500_message);
        }

        Fragment fragment = FailureFeedBackFragment.newInstance(description);
        replaceFragment(R.id.fragment, fragment, tag, false);
    }

    private void initError() {
        if (error == null) {
            description = getString(R.string.http_error_500_message);
        } else {
            switch (error.getStatusCode()) {
                case APIError.HTTP_ERROR_500:
                    description = getString(R.string.http_error_500_message);
                    break;
                case APIError.HTTP_ERROR_400:
                    break;
                case APIError.HTTP_ERROR_404:
                    break;
                case APIError.HTTP_ERROR_409:
                    break;
                default:
                    description = getString(R.string.http_error_500_message);
                    break;

            }
        }
    }

    @Override
    public void onButtonClicked() {
        setResult(Activity.RESULT_OK);
        finish();
    }
}
