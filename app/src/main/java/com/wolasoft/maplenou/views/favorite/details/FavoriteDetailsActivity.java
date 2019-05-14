package com.wolasoft.maplenou.views.favorite.details;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.databinding.DataBindingUtil;

import com.wolasoft.maplenou.R;
import com.wolasoft.maplenou.databinding.ActivityAnnouncementDetailsBinding;
import com.wolasoft.waul.activities.BaseActivity;

public class FavoriteDetailsActivity extends BaseActivity {

    private ActivityAnnouncementDetailsBinding dataBinding;
    public static final String ARG_ANNOUNCEMENT_UUID = "UUID";
    private String uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_announcement_details);
        setSupportActionBar(dataBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().hasExtra(ARG_ANNOUNCEMENT_UUID)) {
            uuid = getIntent().getStringExtra(ARG_ANNOUNCEMENT_UUID);
        }

        replaceFragment(
                R.id.detailsFragment, FavoriteDetailsFragment.newInstance(uuid), "", false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
