package com.wolasoft.maplenou.views.city;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.wolasoft.maplenou.R;
import com.wolasoft.maplenou.data.entities.City;
import com.wolasoft.maplenou.databinding.ActivityCityListBinding;

public class CityListActivity extends AppCompatActivity
        implements CityListFragment.OnFragmentCityListInteractionListener {

    public static final String CITY_KEY = "CITY_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCityListBinding dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_city_list);
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
    public void onCitySelected(City city) {
        Intent intent = new Intent();
        intent.putExtra(CITY_KEY, city);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
