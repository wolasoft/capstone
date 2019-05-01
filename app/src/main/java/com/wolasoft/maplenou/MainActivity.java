package com.wolasoft.maplenou;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wolasoft.maplenou.data.entities.Announcement;
import com.wolasoft.maplenou.data.preferences.AppPreferences;
import com.wolasoft.maplenou.views.account.AccountFragment;
import com.wolasoft.maplenou.views.announcement.create.CreateAnnouncementFragment;
import com.wolasoft.maplenou.views.announcement.details.AnnouncementDetailsActivity;
import com.wolasoft.maplenou.views.announcement.list.AnnouncementListFragment;
import com.wolasoft.maplenou.views.favorite.details.FavoriteDetailsActivity;
import com.wolasoft.maplenou.views.favorite.list.FavoriteListFragment;
import com.wolasoft.maplenou.views.login.LoginFragment;
import com.wolasoft.maplenou.views.message.MessageFragment;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements
        AnnouncementListFragment.OnAnnouncementListFragmentInteractionListener,
        FavoriteListFragment.OnFavoriteListFragmentInteractionListener,
        LoginFragment.OnLoginFragmentInteractionListener {

    private static final String ACCOUNT_FRAGMENT_TAG = "ACCOUNT_FRAGMENT_TAG";
    private static final String ANNOUNCEMENT_CREATION_FRAGMENT_TAG
            = "ANNOUNCEMENT_CREATION_FRAGMENT_TAG";
    private static final String ANNOUNCEMENT_LIST_FRAGMENT_TAG = "ANNOUNCEMENT_LIST_FRAGMENT_TAG";
    private static final String ANNOUNCEMENT_FAVORITE_LIST_FRAGMENT_TAG
            = "ANNOUNCEMENT_FAVORITE_LIST_FRAGMENT_TAG";
    private static final String CURRENT_FRAGMENT_TAG = "CURRENT_FRAGMENT_TAG";
    private static final String LOGIN_FRAGMENT_TAG = "LOGIN_FRAGMENT_TAG";
    private static final String MESSAGE_FRAGMENT_TAG = "MESSAGE_FRAGMENT_TAG";
    private FragmentManager fragmentManager;
    @Inject
    public AppPreferences preferences;
    private String currentTag;
    Fragment currentFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                switch (item.getItemId()) {
                    case R.id.navigation_announcement:
                        addOrReplaceFragment(
                                AnnouncementListFragment.newInstance(),
                                ANNOUNCEMENT_LIST_FRAGMENT_TAG, true);
                        return true;
                    case R.id.navigation_favorite:
                        addOrReplaceFragment(
                                FavoriteListFragment.newInstance(),
                                ANNOUNCEMENT_FAVORITE_LIST_FRAGMENT_TAG, true);
                        return true;
                    case R.id.navigation_add:
                        if (!preferences.isAccountConnected()) {
                            mustLogin();
                            return true;
                        }
                        addOrReplaceFragment(
                                CreateAnnouncementFragment.newInstance(),
                                ANNOUNCEMENT_CREATION_FRAGMENT_TAG, false);
                        return true;
                    case R.id.navigation_message:
                        if (!preferences.isAccountConnected()) {
                            mustLogin();
                            return true;
                        }
                        addOrReplaceFragment(
                                MessageFragment.newInstance(),
                                MESSAGE_FRAGMENT_TAG, false);
                        return true;
                    case R.id.navigation_person:
                        if (!preferences.isAccountConnected()) {
                            mustLogin();
                            return true;
                        }
                        addOrReplaceFragment(
                                AccountFragment.newInstance(),
                                ACCOUNT_FRAGMENT_TAG, false);
                        return true;
                }
                return false;
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaplenouApplication.app().getAppComponent().inject(this);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(CURRENT_FRAGMENT_TAG)) {
                currentTag = savedInstanceState.getString(CURRENT_FRAGMENT_TAG);
            }

            restoreFragment(savedInstanceState);
        } else {
            addOrReplaceFragment(
                    AnnouncementListFragment.newInstance(), ANNOUNCEMENT_LIST_FRAGMENT_TAG, true);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CURRENT_FRAGMENT_TAG, currentTag);
        fragmentManager.putFragment(outState, currentTag, currentFragment);
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

    @Override
    public void onLoginSucceeded() {
        // TODO must be implemented
    }

    private void addOrReplaceFragment(Fragment fragment, String tag, boolean addToBackStack) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(R.id.fragment_container, fragment, tag);

        if (addToBackStack) {
            currentFragment = fragment;
            transaction.addToBackStack(null);
            currentTag = tag;
        }

        transaction.commit();
    }

    private void restoreFragment(Bundle bundle) {
        currentFragment = fragmentManager.getFragment(bundle, currentTag);
        switch (currentTag) {
            case ANNOUNCEMENT_LIST_FRAGMENT_TAG:
                addOrReplaceFragment(currentFragment, ANNOUNCEMENT_LIST_FRAGMENT_TAG, true);
                break;
            case ANNOUNCEMENT_FAVORITE_LIST_FRAGMENT_TAG:
                addOrReplaceFragment(currentFragment, ANNOUNCEMENT_FAVORITE_LIST_FRAGMENT_TAG, true);
                break;
            case LOGIN_FRAGMENT_TAG:
                addOrReplaceFragment(currentFragment, LOGIN_FRAGMENT_TAG, true);
                break;
            case SUBSCRIBE_FRAGMENT_TAG:
                addOrReplaceFragment(currentFragment, SUBSCRIBE_FRAGMENT_TAG, true);
                break;
        }
    }

    private void mustLogin() {
        addOrReplaceFragment(LoginFragment.newInstance(), LOGIN_FRAGMENT_TAG, false);
    }
}
