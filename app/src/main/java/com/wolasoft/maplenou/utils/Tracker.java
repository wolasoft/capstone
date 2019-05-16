package com.wolasoft.maplenou.utils;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.google.firebase.analytics.FirebaseAnalytics;

public class Tracker {
    private FirebaseAnalytics mFirebaseAnalytics;

    public Tracker(Context context) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    public void sendEvent(String eventName, @Nullable Bundle bundle) {
        mFirebaseAnalytics.logEvent(eventName, bundle);
    }

    public void sendFragmentOpenEvent(String fragmentName) {
        Bundle bundle = new Bundle();
        bundle.putString(Params.PARAM_FRAGMENT_NAME, fragmentName);
        mFirebaseAnalytics.logEvent(Event.EVENT_FRAGMENT_OPEN, bundle);
    }

    public static class Event {
        public static final String EVENT_FRAGMENT_OPEN = "fragment_open";
        public static final String EVENT_SEARCH = "event_search";
    }

    public static class Params {
        public static final String PARAM_FRAGMENT_NAME = "param_fragment_name";
    }

    public static class Values {
        public static final String VALUE_SEARCH_FRAGMENT = "search_fragment";
        public static final String VALUE_SEARCH_TITLE = "search_title";
        public static final String VALUE_SEARCH_CATEGORY = "search_category";
        public static final String VALUE_SEARCH_CITY = "search_city";
    }
}
