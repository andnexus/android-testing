package com.andnexus.android.tests;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;

import static com.andnexus.android.tests.MainActivity.Module;

@Singleton
@Component(modules = {Module.class})
public interface Graph {

    void inject(MainActivity activity);

    public final static class Initializer {

        public static Graph init(Context context) {
            return DaggerGraph.builder()
                    .module(new Module(context))
                    .build();
        }
    }
}