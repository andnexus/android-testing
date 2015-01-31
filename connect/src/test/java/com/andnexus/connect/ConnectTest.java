package com.andnexus.connect;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.Collections;

import static com.andnexus.connect.Connect.ConnectException;
import static com.andnexus.connect.Connect.ConnectException.DeliveryException;
import static com.andnexus.connect.Request.RequestBuilder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ConnectTest {

    @Mock
    Parser mParser;

    @Mock
    Request mRequest;

    @Mock
    RequestBuilder mRequestBuilder;

    @Mock
    HttpURLConnection mConnection;

    @Mock
    InputStream mInputStream;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        when(mConnection.getInputStream()).thenReturn(mInputStream);
        when(mRequest.connect()).thenReturn(mConnection);
        when(mRequestBuilder.create()).thenReturn(mRequest);
    }

    @Test
    public void shouldReturnEmptyList() throws ConnectException {

        assertEquals(Collections.EMPTY_LIST, new Connect(mRequestBuilder, mParser).getData());
    }

    @Test
    public void shouldIntegrateParser() throws ConnectException {

        Connect connect = new Connect(mRequestBuilder, mParser);

        connect.getData();
        verify(mParser, times(1)).getModels(mInputStream);
    }

    @Test
    public void shouldExecuteRequest() throws ConnectException {

        Connect connect = new Connect(mRequestBuilder, mParser);
        connect.getData();
        verify(mRequest, times(1)).connect();
    }

    @Test
    public void shouldSetupWithDefaultRequestBuilderAndParser() {

        Connect connect = new Connect("localhost");
        assertEquals("localhost", connect.mRequestBuilder.mHost);
        assertEquals("/backend/data", connect.mRequestBuilder.mPathBuilder.toString());
    }

    @Test
    public void shouldThrowDeliveryException() throws ConnectException {

        // Prepare
        Connect connect = new Connect(new Request.RequestBuilder("host", 80, "/path")
                .setStreamHandler(new URLStreamHandler() {
                    @Override
                    protected URLConnection openConnection(final URL url) throws IOException {
                        when(mConnection.getInputStream()).then(new Answer<Object>() {
                            @Override
                            public Object answer(InvocationOnMock invocation) throws Throwable {
                                throw new IOException();
                            }
                        });
                        return mConnection;
                    }

                }), mParser);

        // Test
        try {
            connect.getData();
            Assert.fail("Expected exception.");
        } catch (ConnectException exception) {
            assertTrue(exception.getCause() instanceof DeliveryException);
        } finally {

            // Reset
            when(mRequest.connect()).thenReturn(mConnection);
        }
    }
}
