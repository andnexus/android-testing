package com.andnexus.android.tests;

import android.test.suitebuilder.annotation.LargeTest;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.when;

@LargeTest
public class MainActivityTest extends ActivityTest {

    private MainActivity mActivity;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        mActivity = getActivity();
    }

    public void testShouldDisplayErrorDialog() {

        when(mConnectivityManager.getActiveNetworkInfo().isConnected()).thenReturn(false);

        onView(withId(R.id.action_send))
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withText(mActivity.getString(R.string.error_title)))
                .check(matches(isDisplayed()));

        onView(withText(mActivity.getString(R.string.error_message)))
                .check(matches(isDisplayed()));

        onView(withText(mActivity.getString(android.R.string.ok)))
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withId(R.id.action_send))
                .check(matches(isDisplayed()));
    }

    public void testOnValidStateSubmission() {

        when(mConnectivityManager.getActiveNetworkInfo().isConnected()).thenReturn(true);

        onView(withId(R.id.action_send))
                .check(matches(isDisplayed()))
                .perform(click())
                .check(matches(isDisplayed()));
    }
}
