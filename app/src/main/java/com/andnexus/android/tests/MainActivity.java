package com.andnexus.android.tests;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Provides;


public class MainActivity extends ActionBarActivity {

    @Inject
    ConnectivityManager mConnectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((App) getApplication()).graph().inject(this);

        setContentView(R.layout.contact);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_send:
                if (isOffline()) {
                    new Dialog().show(getFragmentManager(), "error");
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean isOffline() {
        final NetworkInfo activeNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo == null || !activeNetworkInfo.isConnected();
    }

    public static class Dialog extends DialogFragment {

        public Dialog() {

        }

        @Override
        public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.error_title)
                    .setMessage(R.string.error_message)
                    .setPositiveButton(android.R.string.ok, null)
                    .create();
        }
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

    }

}
