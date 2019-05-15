package com.wolasoft.maplenou.views.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.wolasoft.maplenou.R;
import com.wolasoft.maplenou.data.dto.Search;
import com.wolasoft.maplenou.databinding.ActivitySearchBinding;
import com.wolasoft.waul.activities.BaseActivity;

public class SearchActivity extends BaseActivity
        implements SearchFragment.OnSearchInteractionListener {

    public static final String ARG_SEARCH_PARAMS = "ARG_SEARCH_PARAMS";
    private ActivitySearchBinding dataBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        setSupportActionBar(dataBinding.appBar.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (getIntent().hasExtra(ARG_SEARCH_PARAMS)) {
            Search searchParams = getIntent().getParcelableExtra(ARG_SEARCH_PARAMS);
            replaceFragment(R.id.fragment, SearchFragment.newInstance(searchParams), "", false);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        setResult(Activity.RESULT_CANCELED);
        finish();

        return false;
    }

    @Override
    public void onSearchButtonCLicked(Search searchParams) {
        Intent intent = new Intent();
        intent.putExtra(ARG_SEARCH_PARAMS, searchParams);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
