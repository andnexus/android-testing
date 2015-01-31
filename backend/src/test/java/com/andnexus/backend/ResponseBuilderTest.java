package com.andnexus.backend;

import com.andnexus.model.Data;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ResponseBuilderTest {

    private final Data[] mData;

    private final String mExpected;

    public ResponseBuilderTest(String expected, Data[] data) {
        mExpected = expected;
        mData = data;
    }

    @Parameterized.Parameters
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"{\"data\":[]}", null,},
                {"{\"data\":[]}", new Data[]{}},
                {"{\"data\":[{\"id\":1}]}", new Data[]{new Data(1)}},
                {"{\"data\":[{\"id\":1},{\"id\":2}]}", new Data[]{new Data(1), new Data(2)}},
        });
    }

    @Test
    public void shouldDeliverJsonResponseForData() {
        assertEquals(mExpected, new ResponseBuilder().setData(mData).createJson());
    }
}