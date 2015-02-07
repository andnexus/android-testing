package com.andnexus.connect;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;

import static com.andnexus.connect.Connect.ConnectException;
import static com.andnexus.connect.Connect.ConnectException.HostException;
import static com.andnexus.connect.Connect.ConnectException.RequestException;
import static com.google.common.net.UrlEscapers.urlFormParameterEscaper;

class Request {

    final URL mUrl;

    Request(URL url) {
        mUrl = url;
    }

    public HttpURLConnection connect() throws ConnectException {
        final long start = System.currentTimeMillis();
        try {
            HttpURLConnection connection = (HttpURLConnection) mUrl.openConnection();
            connection.setUseCaches(false);
            connection.setDoOutput(false);
            connection.connect();
            System.out.println(String.format("Connected with URL %s in %dms.", mUrl, (System.currentTimeMillis() - start) / 1000));
            return connection;
        } catch (IOException e) {
            throw new ConnectException(new RequestException(e));
        }
    }

    public static class RequestBuilder {

        final String mHost;

        final StringBuilder mParameterBuilder;

        final StringBuilder mPathBuilder;

        final int mPort;

        final String mProtocol;

        URLStreamHandler mStreamHandler = null;

        public RequestBuilder(String host, int port, String path) {
            this("http", host, port, path);
        }

        public RequestBuilder(String protocol, String host, int port, String path) {
            mProtocol = protocol;
            mHost = host;
            mPort = port;
            mPathBuilder = new StringBuilder(path);
            mParameterBuilder = new StringBuilder();
        }

        final static String getParameterPrefix(StringBuilder stringBuilder) {
            return stringBuilder.length() == 0 ? "?" : "&";
        }

        /**
         * Specifying a handler of null, which is default,  indicates that the URL should use a default stream handler.
         */
        public RequestBuilder setStreamHandler(URLStreamHandler streamHandler) {
            mStreamHandler = streamHandler;
            return this;
        }

        public RequestBuilder addParameter(String key, String value) {
            mParameterBuilder.append(String.format("%s%s=%s", getParameterPrefix(mParameterBuilder), key, urlFormParameterEscaper().escape(value)));
            return this;
        }

        public RequestBuilder appendPath(String path) {
            mPathBuilder.append(path);
            return this;
        }

        protected Request create() throws ConnectException {
            try {
                return new Request(new URL(mProtocol, mHost, mPort, mPathBuilder.toString() + mParameterBuilder.toString(), mStreamHandler));
            } catch (MalformedURLException e) {
                throw new ConnectException(new HostException(e));
            }
        }
    }

}