package com.wolasoft.maplenou;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wolasoft.maplenou.data.entities.Announcement;
import com.wolasoft.maplenou.ui.announcement.AnnouncementListFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity implements
        AnnouncementListFragment.OnAnnouncementListFragmentInteractionListener {

    private static final String ANNOUNCEMENT_LIST_FRAGMENT_TAG = "ANNOUNCEMENT_LIST_FRAGMENT_TAG";
    private FragmentManager fragmentManager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_announcement:
                    addOrReplaceFragment(AnnouncementListFragment.newInstance(), ANNOUNCEMENT_LIST_FRAGMENT_TAG);
                    return true;
                case R.id.navigation_favorite:
                    return true;
                case R.id.navigation_add:
                    return true;
                case R.id.navigation_message:
                    return true;
                case R.id.navigation_person:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragmentManager = getSupportFragmentManager();
        addOrReplaceFragment(AnnouncementListFragment.newInstance(), ANNOUNCEMENT_LIST_FRAGMENT_TAG);
    }

    @Override
    public void onAnnouncementListFragmentInteraction(Announcement announcement) {

    }

    private void addOrReplaceFragment(Fragment fragment, String tag) {
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment, tag)
                //.addToBackStack(null)
                .commit();
    }
}
