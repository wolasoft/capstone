package com.wolasoft.maplenou;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_announcement:
                    mTextMessage.setText(R.string.menu_title_announcement);
                    return true;
                case R.id.navigation_favorite:
                    mTextMessage.setText(R.string.menu_title_favorite);
                    return true;
                case R.id.navigation_add:
                    mTextMessage.setText(R.string.menu_title_add);
                    return true;
                case R.id.navigation_message:
                    mTextMessage.setText(R.string.menu_title_message);
                    return true;
                case R.id.navigation_person:
                    mTextMessage.setText(R.string.menu_title_person);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
