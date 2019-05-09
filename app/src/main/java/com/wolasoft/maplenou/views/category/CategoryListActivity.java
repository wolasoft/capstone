package com.wolasoft.maplenou.views.category;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.wolasoft.maplenou.R;
import com.wolasoft.maplenou.data.entities.Category;
import com.wolasoft.maplenou.databinding.ActivityCategoryListBinding;

public class CategoryListActivity extends AppCompatActivity
        implements CategoryListFragment.OnFragmentCategoryListInteractionListener {

    public static final String CATEGORY_KEY = "CATEGORY_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCategoryListBinding dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_category_list);
        setSupportActionBar(dataBinding.appBar.toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_withe_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        setResult(Activity.RESULT_CANCELED);
        finish();

        return false;
    }

    @Override
    public void onCategorySelected(Category category) {
        Intent intent = new Intent();
        intent.putExtra(CATEGORY_KEY, category);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
