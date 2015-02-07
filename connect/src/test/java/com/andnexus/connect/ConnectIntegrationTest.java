package com.andnexus.connect;

import com.andnexus.model.Data;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ConnectIntegrationTest {

    @Test
    public void backendIntegrationTest() throws Exception {

        Connect connect = new Connect("localhost");
        List<Data> actual = connect.getData();
        assertEquals(2, actual.size());
        assertEquals(123, actual.get(0).getId());
        assertEquals(456, actual.get(1).getId());
    }
}
