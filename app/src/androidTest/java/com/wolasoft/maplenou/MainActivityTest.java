package com.wolasoft.maplenou;

import android.view.View;
import android.widget.FrameLayout;

import androidx.fragment.app.FragmentManager;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.wolasoft.maplenou.views.announcement.list.AnnouncementListFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    private static final String ANNOUNCEMENT_LIST_FRAGMENT_TAG = "ANNOUNCEMENT_LIST_FRAGMENT_TAG";

    private AnnouncementListFragment announcementListFragment;
    private FragmentManager fragmentManager;
    private MainActivity mainActivity;

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() {
        mainActivity = mainActivityTestRule.getActivity();
        announcementListFragment = AnnouncementListFragment.newInstance();
        fragmentManager = mainActivityTestRule.getActivity().getSupportFragmentManager();
    }

    @Test
    public void ensureContainerFragmentIsPresent() {
        View fragmentContainer = mainActivity.findViewById(R.id.fragment_container);
        assertThat(fragmentContainer, notNullValue());
        assertThat(fragmentContainer, instanceOf(FrameLayout.class));
        fragmentManager.beginTransaction();
    }

    @Test
    public void openRecipeListFragment() {
        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()));
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, announcementListFragment, ANNOUNCEMENT_LIST_FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();
        onView(withId(R.id.fragment_container)).check(matches(isDisplayed()));
    }
}
