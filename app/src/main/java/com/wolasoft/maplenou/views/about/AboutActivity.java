package com.wolasoft.maplenou.views.about;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.wolasoft.maplenou.R;
import com.wolasoft.maplenou.databinding.ActivityAboutBinding;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAboutBinding dataBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_about);
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
}
