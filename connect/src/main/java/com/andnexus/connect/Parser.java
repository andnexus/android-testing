package com.andnexus.connect;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;

import com.andnexus.model.Data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.andnexus.connect.Connect.ConnectException;
import static com.andnexus.connect.Connect.ConnectException.ParserException;

class Parser {

    List<Data> parseArray(JSONArray jsonArray) {
        List<Data> data = new ArrayList<Data>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            final JSONObject jsonObject = jsonArray.optJSONObject(i);
            data.add(parseObject(jsonObject));
        }
        return data;
    }

    Data parseObject(final JSONObject jsonObject) {
        return new Data(jsonObject.optInt("id"));
    }

    public List<Data> getModels(final InputStream inputStream) throws ConnectException {
        try {
            final String json = CharStreams.toString(new InputStreamReader(inputStream, Charsets.UTF_8));
            List<Data> data = Collections.EMPTY_LIST;
            if (json.length() > 0) {
                JSONArray jsonArray = new JSONObject(json).optJSONArray("data");
                if (jsonArray != null && jsonArray.length() > 0) {
                    data = parseArray(jsonArray);
                }
            }
            return data;
        } catch (Exception e) {
            throw new ConnectException(new ParserException(e));
        }
    }

}
