package com.andnexus.android.tests;

import com.andnexus.connect.Connect;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.test.ActivityInstrumentationTestCase2;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Provides;

import static org.mockito.Mockito.when;

public abstract class ActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    @Inject
    ConnectivityManager mConnectivityManager;

    @Inject
    Connect mConnect;

    public ActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        App app = (App) getInstrumentation().getTargetContext().getApplicationContext();
        app.graph().inject(this);
    }

    @dagger.Module
    public static final class Module {

        @Mock
        ConnectivityManager mConnectivityManager;

        @Mock
        NetworkInfo mNetworkInfo;

        @Mock
        Connect mConnect;

        public Module() {
            MockitoAnnotations.initMocks(this);
        }

        @Provides
        @Singleton
        public ConnectivityManager provideConnectivityManager() {
            when(mConnectivityManager.getActiveNetworkInfo()).thenReturn(mNetworkInfo);
            return mConnectivityManager;
        }

        @Provides
        @Singleton
        public Connect provideConnect() {
            return mConnect;
        }
    }
}