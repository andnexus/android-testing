package com.andnexus.connect;

import com.google.common.base.Charsets;

import com.andnexus.model.Data;

import junit.framework.Assert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.List;

import static com.andnexus.connect.Connect.ConnectException;
import static com.andnexus.connect.Connect.ConnectException.ParserException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class ParserTest {

    Parser mParser;

    @Before
    public void setUp() throws Exception {
        mParser = new Parser();
    }

    @Test
    public void shouldReturnData() throws ConnectException {
        assertEquals(Collections.EMPTY_LIST, mParser.getModels(new ByteArrayInputStream("".toString().getBytes(Charsets.UTF_8))));
        assertEquals(Collections.EMPTY_LIST, mParser.getModels(new ByteArrayInputStream("{}".toString().getBytes(Charsets.UTF_8))));
        assertEquals(Collections.EMPTY_LIST, mParser.getModels(new ByteArrayInputStream("{data:[]}".toString().getBytes(Charsets.UTF_8))));
    }

    @Test
    public void shouldParseObject() throws JSONException {
        Data model = mParser.parseObject(new JSONObject("{\"id\":1}"));
        assertEquals(1, model.getId());
    }

    @Test
    public void shouldParseArray() throws JSONException {
        List<Data> data = mParser.parseArray(new JSONArray("[{\"id\":1}]"));
        assertEquals(1, data.get(0).getId());
    }

    @Test
    public void shouldThrowParserException() {

        try {
            mParser.getModels(new ByteArrayInputStream("{".toString().getBytes(Charsets.UTF_8)));
            Assert.fail("Expected exception.");
        } catch (ConnectException exception) {
            assertTrue(exception.getCause() instanceof ParserException);
        }
    }
}
