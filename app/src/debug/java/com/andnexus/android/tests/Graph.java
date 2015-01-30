package com.andnexus.android.tests;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;

import static com.andnexus.android.tests.ActivityTest.Module;

@Singleton
@Component(modules = {Module.class})
public interface Graph {

    void inject(MainActivity activity);

    void inject(ActivityTest activity);

    public final static class Initializer {

        public static Graph init(Context context) {
            System.setProperty("dexmaker.dexcache", context.getCacheDir().getPath());
            return Dagger_Graph.builder()
                    .module(new Module())
                    .build();
        }
    }
}