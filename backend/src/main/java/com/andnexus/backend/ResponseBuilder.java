package com.andnexus.backend;

import com.andnexus.model.Data;

import org.json.JSONArray;
import org.json.JSONObject;

public class ResponseBuilder {

    private Data[] mData;

    public final ResponseBuilder setData(Data[] data) {
        mData = data;
        return this;
    }

    public final String createJson() {
        final JSONArray array = new JSONArray();
        if (mData != null) {
            for (Data item : mData) {
                JSONObject object = new JSONObject();
                object.put("id", item.getId());
                array.put(object);
            }
        }
        return new JSONObject().put("data", array).toString();
    }

}
