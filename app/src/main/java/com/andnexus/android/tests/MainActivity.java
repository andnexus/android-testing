package com.andnexus.android.tests;

import com.andnexus.connect.Connect;
import com.andnexus.model.Data;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import butterknife.ButterKnife;
import butterknife.InjectView;
import dagger.Provides;

import static com.andnexus.connect.Connect.ConnectException;

public class MainActivity extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener {

    @Inject
    ConnectivityManager mConnectivityManager;

    @Inject
    Connect mConnect;

    @InjectView(R.id.refresh)
    SwipeRefreshLayout mRefreshView;

    @InjectView(R.id.empty)
    TextView mEmptyView;

    @InjectView(R.id.list)
    ListView mListView;

    ArrayAdapter<Data> mAdapter;

    List<Data> mData = new ArrayList<Data>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        disableLockScreen();

        ((App) getApplication()).graph().inject(this);

        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        onCreateListView();
        onCreateRefreshView();

        onRefresh();
    }

    /**
     * Disable device lock screen for UI testing.
     */
    private void disableLockScreen() {
        // http://developer.android.com/tools/testing/activity_testing.html#UnlockDevice
        final boolean isDebuggable = (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
        if (isDebuggable) {
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            KeyguardManager.KeyguardLock lock = keyguardManager.newKeyguardLock(MainActivity.class.getSimpleName());
            lock.disableKeyguard();
        }
    }

    private void onCreateListView() {
        mListView.setEmptyView(mEmptyView);
        mAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mData);
        mListView.setAdapter(mAdapter);
    }

    private void onCreateRefreshView() {
        mRefreshView.setOnRefreshListener(this);
        mRefreshView.setRefreshing(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            onRefresh();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {

        new AsyncTask<Void, Void, List<Data>>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mData.clear();
                mAdapter.notifyDataSetChanged();
                mEmptyView.setText(null);
            }

            @Override
            protected List<Data> doInBackground(Void... params) {
                List<Data> data = Collections.EMPTY_LIST;
                try {
                    data = mConnect.getData();
                } catch (ConnectException e) {
                    Log.e("Connect", e.getMessage(), e);
                }
                return data;
            }

            @Override
            protected void onPostExecute(List<Data> data) {
                super.onPostExecute(data);
                if (data.size() == 0) {
                    if (isOffline()) {
                        mEmptyView.setText(R.string.offline);
                    } else {
                        mEmptyView.setText(R.string.empty);
                    }
                } else {
                    mData.addAll(data);
                    mAdapter.notifyDataSetChanged();
                }
                mRefreshView.setRefreshing(false);
            }
        }.execute();
    }

    private boolean isOffline() {
        final NetworkInfo activeNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo == null || !activeNetworkInfo.isConnected();
    }

    @dagger.Module
    public final static class Module {

        private final Context mContext;

        public Module(Context context) {
            mContext = context;
        }

        @Provides
        @Singleton
        public ConnectivityManager provideConnectivityManager() {
            return (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        }

        @Provides
        @Singleton
        public Connect provideConnect() {
            return new Connect(mContext.getString(R.string.url_backend));
        }

    }

}
