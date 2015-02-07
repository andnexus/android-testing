package com.andnexus.model;

public class Data {

    private final int mId;

    public Data(int id) {
        mId = id;
    }

    @Override
    public String toString() {
        return String.format("%d", getId());
    }

    public int getId() {
        return mId;
    }

}
