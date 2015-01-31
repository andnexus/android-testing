package com.andnexus.connect;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import static com.andnexus.connect.Connect.ConnectException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class RequestTest {

    @Mock
    HttpURLConnection mHttpURLConnection;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldTest() {
        assertEquals("?", Request.RequestBuilder.getParameterPrefix(new StringBuilder()));
        assertEquals("&", Request.RequestBuilder.getParameterPrefix(new StringBuilder("?name=value")));
    }

    @Test
    public void shouldBuildRequest() throws ConnectException {

        // Prepare
        Request request = new Request.RequestBuilder("example.com", 80, "/data")
                .addParameter("param", "?& ")
                .appendPath("/test")
                .create();

        // Pre execution test
        assertEquals("example.com", request.mUrl.getHost());
        assertEquals("/data/test", request.mUrl.getPath());
        assertEquals("param=%3F%26+", request.mUrl.getQuery());
    }

    @Test
    public void shouldDispatchRequest() throws ConnectException, IOException {

        // Prepare
        Request request = new Request.RequestBuilder("host", 80, "/path")
                .setStreamHandler(new URLStreamHandler() {
                    @Override
                    protected URLConnection openConnection(final URL url) throws IOException {
                        when(mHttpURLConnection.getInputStream()).thenReturn(new ByteArrayInputStream("{}".getBytes(Charsets.UTF_8)));
                        return mHttpURLConnection;
                    }
                })
                .create();
        HttpURLConnection connection = request.connect();

        // Post execution test
        assertEquals("{}", CharStreams.toString(new InputStreamReader(connection.getInputStream(), Charsets.UTF_8)));
    }

    @Test
    public void shouldThrowHostException() {

        try {
            new Request.RequestBuilder("", "host", 80, "/path").create();
            Assert.fail("Expected exception.");
        } catch (ConnectException exception) {
            assertTrue(exception.getCause() instanceof ConnectException.HostException);
        }
    }

    @Test
    public void shouldThrowRequestException() throws ConnectException, IOException {

        // Prepare
        Request request = new Request.RequestBuilder("host", 80, "/path")
                .setStreamHandler(new URLStreamHandler() {
                    @Override
                    protected URLConnection openConnection(final URL url) throws IOException {
                        throw new IOException();
                    }
                })
                .create();
        try {
            request.connect();
            Assert.fail("Expected exception.");
        } catch (ConnectException exception) {
            assertTrue(exception.getCause() instanceof ConnectException.RequestException);
        }
    }
}
