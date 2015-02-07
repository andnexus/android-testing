package com.andnexus.test.model;

import com.andnexus.model.Data;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class DataTest {

    private final Data mData;

    private final String mExpected;

    public DataTest(String expected, Data actual) {
        mExpected = expected;
        mData = actual;
    }

    @Parameters
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"0", new Data(0)}
        });
    }

    @Test
    public void shouldCompareOnlyIds() {
        assertEquals(mExpected, mData.toString());
    }
}
