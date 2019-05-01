package com.wolasoft.maplenou.views.account.subscribe;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import com.wolasoft.maplenou.R;

import androidx.appcompat.app.AppCompatActivity;

public class SubscribeSuccessActivity extends AppCompatActivity implements
        SubscribeSuccessFragment.OnSuccessFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe_success);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onTerminateButtonClicked() {
        setResult(Activity.RESULT_OK);
        finish();
    }
}
