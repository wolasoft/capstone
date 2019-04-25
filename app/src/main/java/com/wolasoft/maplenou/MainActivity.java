package com.wolasoft.maplenou;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wolasoft.maplenou.data.entities.Announcement;
import com.wolasoft.maplenou.ui.announcement.favorite.details.FavoriteDetailsActivity;
import com.wolasoft.maplenou.ui.announcement.favorite.list.AnnouncementFavoriteListFragment;
import com.wolasoft.maplenou.ui.announcement.list.AnnouncementListFragment;
import com.wolasoft.maplenou.ui.announcement.details.AnnouncementDetailsActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity implements
        AnnouncementListFragment.OnAnnouncementListFragmentInteractionListener,
        AnnouncementFavoriteListFragment.OnFavoriteListFragmentInteractionListener {

    private static final String ANNOUNCEMENT_LIST_FRAGMENT_TAG = "ANNOUNCEMENT_LIST_FRAGMENT_TAG";
    private static final String ANNOUNCEMENT_FAVORITE_LIST_FRAGMENT_TAG
            = "ANNOUNCEMENT_FAVORITE_LIST_FRAGMENT_TAG";
    private FragmentManager fragmentManager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                switch (item.getItemId()) {
                    case R.id.navigation_announcement:
                        addOrReplaceFragment(
                                AnnouncementListFragment.newInstance(),
                                ANNOUNCEMENT_LIST_FRAGMENT_TAG);
                        return true;
                    case R.id.navigation_favorite:
                        addOrReplaceFragment(
                                AnnouncementFavoriteListFragment.newInstance(),
                                ANNOUNCEMENT_FAVORITE_LIST_FRAGMENT_TAG);
                        return true;
                    case R.id.navigation_add:
                        return true;
                    case R.id.navigation_message:
                        return true;
                    case R.id.navigation_person:
                        return true;
                }
                return false;
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
        Intent intent = new Intent(this, AnnouncementDetailsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onFavoriteListFragmentInteraction(Announcement announcement) {
        Intent intent = new Intent(this, FavoriteDetailsActivity.class);
        startActivity(intent);
    }

    private void addOrReplaceFragment(Fragment fragment, String tag) {
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment, tag)
                .addToBackStack(null)
                .commit();
    }
}
