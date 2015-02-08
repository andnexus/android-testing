package com.andnexus.android.tests;

import com.andnexus.model.Data;

import android.os.AsyncTask;
import android.support.annotation.IdRes;
import android.support.test.espresso.Espresso;
import android.test.suitebuilder.annotation.LargeTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.andnexus.connect.Connect.ConnectException;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@LargeTest
public class MainActivityTest extends ActivityTest {

    MainActivity mActivity;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        mActivity = getActivity();
    }

    private void refresh() {
        onView(withId(R.id.action_refresh))
                .perform(click());
    }

    public void testShouldDisplayOfflineHint() throws Throwable {

        // Prepare
        when(mConnect.getData()).thenReturn(Collections.EMPTY_LIST);
        when(mConnectivityManager.getActiveNetworkInfo().isConnected()).thenReturn(false);

        // Test
        refresh();

        onView(withText(mActivity.getString(R.string.offline)))
                .check(matches(isDisplayed()));
    }

    public void testShouldDisplayOfflineHintAfterSuccessfullyLoadedData() throws ConnectException {

        // Prepare
        when(mConnect.getData()).thenReturn(Arrays.<Data>asList(new Data[]{new Data(1)}));
        when(mConnectivityManager.getActiveNetworkInfo().isConnected()).thenReturn(true);

        // Test
        refresh();

        when(mConnect.getData()).thenReturn(Collections.EMPTY_LIST);
        when(mConnectivityManager.getActiveNetworkInfo().isConnected()).thenReturn(false);

        refresh();

        onView(withText(mActivity.getString(R.string.offline)))
                .check(matches(isDisplayed()));

    }

    public void testShouldDisplayNoDataHint() throws ConnectException {

        // Prepare
        when(mConnect.getData()).thenReturn(Collections.EMPTY_LIST);
        when(mConnectivityManager.getActiveNetworkInfo().isConnected()).thenReturn(true);

        // Test
        refresh();

        onView(withText(mActivity.getString(R.string.empty)))
                .check(matches(isDisplayed()));
    }

    public void testShouldDisplayData() throws ConnectException {

        // Prepare
        List<Data> data = new ArrayList<Data>();
        for (int i = 0; i < 100; i++) {
            data.add(new Data(i));
        }
        when(mConnect.getData()).thenReturn(data);
        when(mConnectivityManager.getActiveNetworkInfo().isConnected()).thenReturn(true);

        // Test
        refresh();

        onData(R.id.list, 99, "99");
    }

    public void testShouldNotExecuteTaskIfIsRunning() throws ConnectException {

        when(mConnectivityManager.getActiveNetworkInfo().isConnected()).thenReturn(true);

        // Should refresh
        when(mConnect.getData()).thenReturn(Arrays.asList(new Data[]{new Data(1)}));
        mActivity.mTask = null;

        refresh();
        onData(R.id.list, 0, "1");

        // Should not refresh
        when(mConnect.getData()).thenReturn(Arrays.<Data>asList(new Data[]{new Data(2)}));
        mActivity.mTask = mock(AsyncTask.class);

        refresh();
        onData(R.id.list, 0, "1");
    }

    void onData(@IdRes int res, int position, String expected) {
        Espresso.onData(allOf(is(instanceOf(Data.class))))
                .inAdapterView(withId(res))
                .atPosition(position)
                .check(matches(withText(expected)));
    }
}
