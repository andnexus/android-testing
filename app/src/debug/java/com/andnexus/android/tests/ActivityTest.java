package com.andnexus.android.tests;

import com.andnexus.connect.Connect;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.app.Activity;
import android.app.KeyguardManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.PowerManager;
import android.test.ActivityInstrumentationTestCase2;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Provides;

import static android.content.Context.KEYGUARD_SERVICE;
import static android.content.Context.POWER_SERVICE;
import static android.os.PowerManager.ACQUIRE_CAUSES_WAKEUP;
import static android.os.PowerManager.FULL_WAKE_LOCK;
import static android.os.PowerManager.ON_AFTER_RELEASE;
import static android.view.WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
import static com.andnexus.android.tests.MainActivity.OnCreateListener;
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

        @Provides
        @Singleton
        public OnCreateListener provideOnCreateListener() {
            return new OnCreateListener() {
                @Override
                public void onCreate(Activity activity) {

                    // http://developer.android.com/tools/testing/activity_testing.html#UnlockDevice
                    KeyguardManager keyguardManager = (KeyguardManager) activity.getSystemService(KEYGUARD_SERVICE);
                    KeyguardManager.KeyguardLock lock = keyguardManager.newKeyguardLock(MainActivity.class.getSimpleName());
                    lock.disableKeyguard();

                    // https://gist.github.com/JakeWharton/f50f3b4d87e57d8e96e9
                    activity.getWindow().addFlags(FLAG_SHOW_WHEN_LOCKED);
                    PowerManager power = (PowerManager) activity.getSystemService(POWER_SERVICE);
                    PowerManager.WakeLock wakeLock = power.newWakeLock(FULL_WAKE_LOCK | ACQUIRE_CAUSES_WAKEUP | ON_AFTER_RELEASE, "wakeup!");
                    wakeLock.acquire();
                    wakeLock.release();
                }
            };
        }

    }
}