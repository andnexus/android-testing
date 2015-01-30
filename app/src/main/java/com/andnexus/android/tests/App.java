package com.andnexus.android.tests;


import android.app.Application;

public class App extends Application {

    private Graph mGraph;

    @Override
    public void onCreate() {
        super.onCreate();

        mGraph = Graph.Initializer.init(this);
    }

    public Graph graph() {
        return mGraph;
    }

}