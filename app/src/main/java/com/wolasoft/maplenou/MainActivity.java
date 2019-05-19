package com.wolasoft.maplenou;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.wolasoft.maplenou.data.entities.Announcement;
import com.wolasoft.maplenou.data.preferences.AppPreferences;
import com.wolasoft.maplenou.databinding.ActivityMainBinding;
import com.wolasoft.maplenou.utils.Tracker;
import com.wolasoft.maplenou.views.account.details.AccountDetailsFragment;
import com.wolasoft.maplenou.views.account.subscribe.SubscribeFragment;
import com.wolasoft.maplenou.views.account.subscribe.SubscribeSuccessActivity;
import com.wolasoft.maplenou.views.announcement.create.CreateAnnouncementFragment;
import com.wolasoft.maplenou.views.announcement.details.AnnouncementDetailsActivity;
import com.wolasoft.maplenou.views.announcement.list.AnnouncementListFragment;
import com.wolasoft.maplenou.views.favorite.details.FavoriteDetailsActivity;
import com.wolasoft.maplenou.views.favorite.list.FavoriteListFragment;
import com.wolasoft.maplenou.views.login.LoginFragment;
import com.wolasoft.maplenou.views.message.MessageFragment;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements
        AnnouncementListFragment.OnAnnouncementListFragmentInteractionListener,
        FavoriteListFragment.OnFavoriteListFragmentInteractionListener,
        LoginFragment.OnLoginFragmentInteractionListener,
        SubscribeFragment.OnFragmentSubscribeInteractionListener,
        AccountDetailsFragment.OnFragmentAccountDetailsInteractionListener {

    private static final String ACCOUNT_FRAGMENT_TAG = "ACCOUNT_FRAGMENT_TAG";
    private static final String ANNOUNCEMENT_CREATION_FRAGMENT_TAG
            = "ANNOUNCEMENT_CREATION_FRAGMENT_TAG";
    private static final String ANNOUNCEMENT_LIST_FRAGMENT_TAG = "ANNOUNCEMENT_LIST_FRAGMENT_TAG";
    private static final String ANNOUNCEMENT_FAVORITE_LIST_FRAGMENT_TAG
            = "ANNOUNCEMENT_FAVORITE_LIST_FRAGMENT_TAG";
    private static final String CURRENT_FRAGMENT_TAG = "CURRENT_FRAGMENT_TAG";
    private static final String LOGIN_FRAGMENT_TAG = "LOGIN_FRAGMENT_TAG";
    private static final String MESSAGE_FRAGMENT_TAG = "MESSAGE_FRAGMENT_TAG";
    private static final String SUBSCRIBE_FRAGMENT_TAG = "SUBSCRIBE_FRAGMENT_TAG";
    private static final String CURRENT_TAB_ID = "CURRENT_TAB_ID";
    public static int ACCOUNT_CREATED_REQUEST_CODE = 1;
    private FragmentManager fragmentManager;
    private BottomNavigationView navigation;
    private ActivityMainBinding dataBinding;
    @Inject
    public AppPreferences preferences;
    @Inject
    public Tracker tracker;
    private Fragment currentFragment;
    private String currentTag;
    private int currentTabId;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                switch (item.getItemId()) {
                    case R.id.navigation_announcement:
                        currentTabId = R.id.navigation_announcement;
                        addOrReplaceFragment(
                                AnnouncementListFragment.newInstance(),
                                ANNOUNCEMENT_LIST_FRAGMENT_TAG, false);
                        return true;
                    case R.id.navigation_favorite:
                        currentTabId = R.id.navigation_favorite;
                        addOrReplaceFragment(
                                FavoriteListFragment.newInstance(),
                                ANNOUNCEMENT_FAVORITE_LIST_FRAGMENT_TAG, false);
                        return true;
                    case R.id.navigation_add:
                        currentTabId = R.id.navigation_add;
                        if (!preferences.isAccountConnected()) {
                            mustLogin();
                            return true;
                        }
                        addOrReplaceFragment(
                                CreateAnnouncementFragment.newInstance(),
                                ANNOUNCEMENT_CREATION_FRAGMENT_TAG, false);
                        return true;
                    case R.id.navigation_message:
                        currentTabId = R.id.navigation_message;
                        if (!preferences.isAccountConnected()) {
                            mustLogin();
                            return true;
                        }
                        addOrReplaceFragment(
                                MessageFragment.newInstance(),
                                MESSAGE_FRAGMENT_TAG, false);
                        return true;
                    case R.id.navigation_person:
                        currentTabId = R.id.navigation_person;
                        if (!preferences.isAccountConnected()) {
                            mustLogin();
                            return true;
                        }
                        addOrReplaceFragment(
                                AccountDetailsFragment.newInstance(),
                                ACCOUNT_FRAGMENT_TAG, false);
                        return true;
                }
                return false;
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        MaplenouApplication.app().getAppComponent().inject(this);
        tracker.sendEvent(FirebaseAnalytics.Event.APP_OPEN, null);
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        setSupportActionBar(dataBinding.appBar.toolbar);

        MobileAds.initialize(this, getString(R.string.admob_id));

        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState != null) {
            currentTabId = savedInstanceState.getInt(CURRENT_TAB_ID);
            currentTag = savedInstanceState.getString(CURRENT_FRAGMENT_TAG);

            restoreFragment();
        } else {
            addOrReplaceFragment(
                    AnnouncementListFragment.newInstance(), ANNOUNCEMENT_LIST_FRAGMENT_TAG, false);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CURRENT_FRAGMENT_TAG, currentTag);
        outState.putInt(CURRENT_TAB_ID, currentTabId);
    }

    @Override
    public void onAnnouncementClicked(Announcement announcement) {
        Intent intent = new Intent(this, AnnouncementDetailsActivity.class);
        intent.putExtra(AnnouncementDetailsActivity.ARG_ANNOUNCEMENT_UUID, announcement.getUuid());
        startActivity(intent);
    }

    @Override
    public void onFavoriteAnnouncementSelected(Announcement announcement) {
        Intent intent = new Intent(this, FavoriteDetailsActivity.class);
        intent.putExtra(FavoriteDetailsActivity.ARG_ANNOUNCEMENT_UUID, announcement.getUuid());
        startActivity(intent);
    }

    @Override
    public void onLoginSucceeded() {
        navigation.setSelectedItemId(currentTabId);
    }

    @Override
    public void onSubscribeClicked() {
        addOrReplaceFragment(SubscribeFragment.newInstance(), SUBSCRIBE_FRAGMENT_TAG, false);
    }

    @Override
    public void onAccountCreated() {
        Intent intent = new Intent(this, SubscribeSuccessActivity.class);
        startActivityForResult(intent, ACCOUNT_CREATED_REQUEST_CODE);
    }

    @Override
    public void onDisconnect() {
        addOrReplaceFragment(LoginFragment.newInstance(), LOGIN_FRAGMENT_TAG, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void addOrReplaceFragment(Fragment fragment, String tag, boolean addToBackStack) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(R.id.fragment_container, fragment, tag);
        currentTag = tag;

        tracker.sendFragmentOpenEvent(tag);

        if (addToBackStack) {
            currentFragment = fragment;
            transaction.addToBackStack(null);
        }

        transaction.commitAllowingStateLoss();
    }

    private void restoreFragment() {
        navigation.setSelectedItemId(currentTabId);
        switch (currentTag) {
            case LOGIN_FRAGMENT_TAG:
                addOrReplaceFragment(LoginFragment.newInstance(), LOGIN_FRAGMENT_TAG, true);
                break;
            case SUBSCRIBE_FRAGMENT_TAG:
                addOrReplaceFragment(SubscribeFragment.newInstance(), SUBSCRIBE_FRAGMENT_TAG, true);
                break;
        }
    }

    private void mustLogin() {
        addOrReplaceFragment(LoginFragment.newInstance(), LOGIN_FRAGMENT_TAG, false);
    }
}
